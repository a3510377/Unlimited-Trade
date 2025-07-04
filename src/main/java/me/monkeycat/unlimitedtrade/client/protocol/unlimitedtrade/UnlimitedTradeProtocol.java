package me.monkeycat.unlimitedtrade.client.protocol.unlimitedtrade;

import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.protocol.BaseProtocol;
import me.monkeycat.unlimitedtrade.common.network.MerchantEntityStatusPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeHelloPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeStartWatchPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeStopWatchPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class UnlimitedTradeProtocol extends BaseProtocol {
    public static final int PROTOCOL_VERSION = 1_0_0;
    @Nullable
    private MerchantEntity currentMerchantEntity = null;
    @Nullable
    private Entity.RemovalReason currentRemovalReason = null;

    public void init() {
        ClientPlayNetworking.registerGlobalReceiver(UnlimitedTradeHelloPayload.ID, this::handleHello);
        ClientPlayNetworking.registerGlobalReceiver(MerchantEntityStatusPayload.ID, this::handleMerchantEntityStatus);
    }

    public void handleHello(UnlimitedTradeHelloPayload payload, ClientPlayNetworking.Context context) {
        if (payload.version() == PROTOCOL_VERSION) {
            UnlimitedTradeMod.LOGGER.info("UnlimitedTrade connection successful");
            enabled = true;
        } else {
            UnlimitedTradeMod.LOGGER.info("UnlimitedTrade connection failed, version: {}", payload.version());
            enabled = false;
        }
    }

    public void handleMerchantEntityStatus(MerchantEntityStatusPayload payload, ClientPlayNetworking.Context context) {
        enabled = true;
        if (currentMerchantEntity != null && currentMerchantEntity.getUuid().equals(payload.uuid())) {
            currentRemovalReason = payload.removalReason();
        } else {
            currentRemovalReason = null;
            UnlimitedTradeMod.LOGGER.warn("Received merchant entity status for an unknown merchant: {}", payload.uuid());
        }
    }

    private void trySendPayload(Supplier<CustomPayload> supplier) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.networkHandler.sendPacket(new CustomPayloadC2SPacket(supplier.get()));
        }
    }

    @Nullable
    public MerchantEntity getCurrentMerchantEntity() {
        return currentMerchantEntity;
    }

    @Override
    public void startWatching(MerchantEntity merchantEntity) {
        if (Boolean.TRUE.equals(enabled) && currentMerchantEntity != merchantEntity) {
            trySendPayload(() -> new UnlimitedTradeStartWatchPayload(merchantEntity.getWorld().getRegistryKey(), merchantEntity.getUuid()));
        }
        currentMerchantEntity = merchantEntity;
    }

    @Override
    public void stopWatching() {
        if (Boolean.TRUE.equals(enabled) && currentMerchantEntity != null) {
            trySendPayload(() -> new UnlimitedTradeStopWatchPayload(currentMerchantEntity.getUuid()));
        }
        currentMerchantEntity = null;
    }

    @Override
    public boolean canTrade(MerchantEntity merchantEntity) {
        if (currentRemovalReason == null) {
            setStatusChangeFlag(true);
            return false;
        }

        return Boolean.TRUE.equals(enabled) && currentMerchantEntity != null && currentMerchantEntity.getUuid().equals(merchantEntity.getUuid()) && currentRemovalReason != Entity.RemovalReason.KILLED;
    }
}
