package me.monkeycat.unlimitedtrade;

import me.monkeycat.unlimitedtrade.common.network.MerchantEntityStatusPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeHelloPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeStartWatchPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeStopWatchPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnlimitedTradeMod implements ModInitializer {
    public static final String MOD_ID = "unlimitedtrade";
    public static final String MOD_NAME = "UnlimitedTrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static String VERSION = "unknown";

    @Override
    public void onInitialize() {
        VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

        PayloadTypeRegistry.playS2C().register(UnlimitedTradeHelloPayload.ID, UnlimitedTradeHelloPayload.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(MerchantEntityStatusPayload.ID, MerchantEntityStatusPayload.PACKET_CODEC);

        PayloadTypeRegistry.playC2S().register(UnlimitedTradeStopWatchPayload.ID, UnlimitedTradeStopWatchPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(UnlimitedTradeStartWatchPayload.ID, UnlimitedTradeStartWatchPayload.PACKET_CODEC);
    }
}
