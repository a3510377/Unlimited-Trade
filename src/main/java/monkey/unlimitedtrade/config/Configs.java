package monkey.unlimitedtrade.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import monkey.unlimitedtrade.AutoTradeModClient;
import monkey.unlimitedtrade.config.options.*;
import monkey.unlimitedtrade.config.types.AfterTradeActions;
import monkey.unlimitedtrade.gui.AutoTradGuiConfigsBase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Configs {
    public static final int VERSION = 1;
    public static final List<AutoTradeConfigOption> OPTIONS = new ArrayList<>();
    public static final Map<Config.Category, List<AutoTradeConfigOption>> CATEGORY_TO_OPTION = Maps.newLinkedHashMap();

    /* ------------------------------ */
    /* ----------- Hot Key ---------- */
    /* ------------------------------ */
//    @Hotkey(hotkey = "V,B")
    @Config(category = Config.Category.SETTING)
    public static AutoTradeHotKeyed openConfigGui = new AutoTradeHotKeyed("openConfigGui", "V,B");

    /* ------------------------------ */
    /* ----------- setting ---------- */
    /* ------------------------------ */
    @Config(category = Config.Category.SETTING)
    public static AutoTradeBooleanHotKeyed startTrade = new AutoTradeBooleanHotKeyed("startTrade", "C,V");
    @Config(category = Config.Category.SETTING)
    public static AutoTradeBooleanHotKeyed waitChunkDebug = new AutoTradeBooleanHotKeyed("waitChunkDebug", "");
    @Config(category = Config.Category.SETTING)
    public static AutoTradeStringList dropBlockList = new AutoTradeStringList(
            "dropBlockList",
            ImmutableList.copyOf(new ArrayList<>()));
    @Config(category = Config.Category.SETTING)
    public static AutoTradeOptionList afterTradeActions = new AutoTradeOptionList(
            "afterTradeActions",
            AfterTradeActions.USE_AND_DROP);

    static {
        for (Field field : Configs.class.getDeclaredFields()) {
            Config annotation = field.getAnnotation(Config.class);
            if (annotation != null) {
                try {
                    Object config = field.get(null);
                    if (!(config instanceof AutoTradeIConfigBase)) {
                        AutoTradeModClient.LOGGER.warn("{} is not subclass of AutoTradeIConfigBase", config);
                        continue;
                    }

                    AutoTradeConfigOption option = new AutoTradeConfigOption(annotation, (AutoTradeIConfigBase) config);
                    OPTIONS.add(option);
                    CATEGORY_TO_OPTION.computeIfAbsent(option.getCategory(), k -> Lists.newArrayList()).add(option);
                } catch (IllegalAccessException e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }

            }
        }
    }

    public static void init() {
        InitializationHandler.getInstance().registerInitializationHandler(() -> {
            ConfigManager.getInstance().registerConfigHandler(AutoTradeModClient.MOD_ID, AutoTradeConfigStorage.getInstance());

            InputEventHandler.getKeybindManager().registerKeybindProvider(new KeybindProvider());

            openConfigGui.getKeybind().setCallback(((keyAction, iKeybind) -> {
                AutoTradGuiConfigsBase.openGui();

                return true;
            }));
        });
    }
}
