package monkey.unlimitedtrade.config;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import monkey.unlimitedtrade.config.types.AfterTradeActions;
import monkey.unlimitedtrade.gui.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Hotkey;
import top.hendrixshen.magiclib.malilib.api.annotation.Numeric;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;

import java.util.ArrayList;

public class Configs {
    public static final int VERSION = 1;

    /* ------------------------------ */
    /* ----------- Hot Key ---------- */
    /* ------------------------------ */
    @Hotkey(hotkey = "V,B")
    @Config(category = Category.SETTING)
    public static ConfigHotkey openConfigGui;

    @Hotkey(hotkey = "C,V")
    @Config(category = Category.SETTING)
    public static boolean startTrade;

    /* ------------------------------ */
    /* ----------- setting ---------- */
    /* ------------------------------ */

    @Config(category = Category.SETTING)
    public static boolean waitChunkDebug = true;

    @Config(category = Category.SETTING)
    public static ArrayList<String> dropBlockList = new ArrayList<>();

    @Config(category = Category.SETTING)
    public static AfterTradeActions afterTradeActions = AfterTradeActions.USE_AND_DROP;

    @Numeric(minValue = 0, maxValue = 100, useSlider = true)
    @Config(category = Category.SETTING)
    public static int maxUseRetries = 20;

    public static void init(ConfigManager cm) {
        openConfigGui.getKeybind().setCallback(((keyAction, iKeybind) -> {
            ConfigScreen screen = ConfigScreen.instance;

            MinecraftClient.getInstance().setScreen(screen);

            return true;
        }));
    }

    public static class Category {
        public static final String SETTING = "setting";
    }
}
