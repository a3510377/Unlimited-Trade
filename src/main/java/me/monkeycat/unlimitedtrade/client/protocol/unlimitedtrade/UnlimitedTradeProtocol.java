package me.monkeycat.unlimitedtrade.client.protocol.unlimitedtrade;

import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.protocol.BaseProtocol;
import me.monkeycat.unlimitedtrade.common.network.MerchantEntityStatusPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeHelloPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.passive.MerchantEntity;
import org.jetbrains.annotations.Nullable;

public class UnlimitedTradeProtocol extends BaseProtocol {
    public static final int PROTOCOL_VERSION = 1_0_0;
    @Nullable
    public static Boolean enable = null;

    public void init() {
        ClientPlayNetworking.registerGlobalReceiver(UnlimitedTradeHelloPayload.ID, this::handleHello);
        ClientPlayNetworking.registerGlobalReceiver(MerchantEntityStatusPayload.ID, this::handleMerchantEntityStatus);
    }

    public void handleHello(UnlimitedTradeHelloPayload payload, ClientPlayNetworking.Context context) {
        if (payload.version() == PROTOCOL_VERSION) {
            UnlimitedTradeMod.LOGGER.info("UnlimitedTrade connection successful");
            enable = true;
        } else {
            UnlimitedTradeMod.LOGGER.info("UnlimitedTrade connection failed, version: {}", payload.version());
            enable = false;
        }
    }

    public void handleMerchantEntityStatus(MerchantEntityStatusPayload payload, ClientPlayNetworking.Context context) {
        enable = true;
    }

    @Override
    public void startWatching(MerchantEntity merchantEntity) {
    }

    @Override
    public void stopWatching() {
    }

    @Override
    public boolean canTrade(MerchantEntity merchantEntity) {
        return false;
    }
}
