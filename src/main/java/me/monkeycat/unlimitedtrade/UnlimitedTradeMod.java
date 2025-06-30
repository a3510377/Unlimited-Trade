package me.monkeycat.unlimitedtrade;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnlimitedTradeMod implements ModInitializer {
    public static final String MOD_ID = "unlimitedtrade";
    public static final String MOD_NAME = "UnlimitedTrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static String VERSION = "unknown";
    private static UnlimitedTradeMod instance;

    public static UnlimitedTradeMod getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this;
        VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
    }
}
