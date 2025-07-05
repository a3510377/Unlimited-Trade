package me.monkeycat.unlimitedtrade.client.protocol.unlimitedtrade;

import me.fallenbreath.fanetlib.api.packet.FanetlibPackets;
import me.fallenbreath.fanetlib.api.packet.PacketHandlerS2C;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.protocol.BaseProtocol;
import me.monkeycat.unlimitedtrade.common.network.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class UnlimitedTradeProtocol extends BaseProtocol {
    public static final int PROTOCOL_VERSION = 1_0_0;
    @Nullable
    private MerchantEntity currentMerchantEntity = null;
    @Nullable
    private Entity.RemovalReason currentRemovalReason = null;

    public void init() {
        System.out.println("Registering UnlimitedTrade protocol handlers");

        FanetlibPackets.registerS2C(UnlimitedTradeHelloPayload.ID, UnlimitedTradeHelloPayload.PACKET_CODEC, this::handleHello);
        FanetlibPackets.registerS2C(MerchantEntityStatusPayload.ID, MerchantEntityStatusPayload.PACKET_CODEC, this::handleMerchantEntityStatus);
    }

    public void handleHello(UnlimitedTradeHelloPayload payload, PacketHandlerS2C.Context context) {
        if (payload.version() == PROTOCOL_VERSION) {
            UnlimitedTradeMod.LOGGER.info("UnlimitedTrade connection successful");
            enabled = true;
        } else {
            UnlimitedTradeMod.LOGGER.info("UnlimitedTrade connection failed, version: {}", payload.version());
            enabled = false;
        }
    }

    public void handleMerchantEntityStatus(MerchantEntityStatusPayload payload, PacketHandlerS2C.Context context) {
        enabled = true;
        if (currentMerchantEntity != null && currentMerchantEntity.getUuid().equals(payload.uuid())) {
            currentRemovalReason = payload.removalReason();
        } else {
            currentRemovalReason = null;
            UnlimitedTradeMod.LOGGER.warn("Received merchant entity status for an unknown merchant: {}", payload.uuid());
        }
    }

    private <P extends UnlimitedTradBasePayload<P>> void trySendPayload(Supplier<P> supplier) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            P payload = supplier.get();
            player.networkHandler.sendPacket(FanetlibPackets.createC2S(payload.getId(), payload));
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
