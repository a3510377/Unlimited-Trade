package me.monkeycat.unlimitedtrade.client.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.config.gui.CustomConfigBaseGUI;
import me.monkeycat.unlimitedtrade.client.config.options.*;
import me.monkeycat.unlimitedtrade.client.config.types.AfterTradeActions;
import me.monkeycat.unlimitedtrade.client.config.types.WaitProtoTypes;
import me.monkeycat.unlimitedtrade.client.utils.RegistryUtils;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//#if MC >= 12101
import fi.dy.masa.malilib.util.data.ModInfo;
//#endif

import static me.monkeycat.unlimitedtrade.client.config.ConfigFactory.*;

public class Configs {
    public static final List<CustomConfigOption> OPTIONS = new ArrayList<>();
    public static final Map<Config.Type, List<CustomConfigOption>> TYPE_LIST_MAP = Maps.newLinkedHashMap();
    public static final Map<Config.Category, List<CustomConfigOption>> CATEGORY_TO_OPTION = Maps.newLinkedHashMap();

    @Config(type = Config.Type.HOTKEY, category = Config.Category.SETTING)
    public static final CustomConfigHotkey OPEN_CONFIG_GUI = newConfigHotKey("openConfigGui", "V,B");
    @Config(type = Config.Type.HOTKEY, category = Config.Category.SETTING)
    public static final CustomConfigBoolHotkey START_TRADE = newConfigBoolHotkey("startTrade", false, "C,V");
    @Config(type = Config.Type.LIST, category = Config.Category.SETTING)
    public static final CustomConfigStringList DROP_BLOCK_LIST = newConfigStringList("dropBlockList", RegistryUtils.getItemIds(ImmutableList.of(Items.EMERALD)));
    @Config(type = Config.Type.LIST, category = Config.Category.SETTING)
    public static final CustomConfigStringList AFTER_USE_WHITE_LIST = newConfigStringList("afterUseWhiteList", RegistryUtils.getBlockIds(ImmutableList.of(Blocks.TRAPPED_CHEST, Blocks.NOTE_BLOCK)));
    public static final ItemRestriction DROP_BLOCK_LIST_RESTRICTION = new ItemRestriction();
    @Config(type = Config.Type.LIST, category = Config.Category.SETTING)
    public static final CustomConfigOptionList AFTER_TRADE_ACTIONS = newConfigOptionList("afterTradeActions", AfterTradeActions.OFF.getDefault());
    @Config(type = Config.Type.LIST, category = Config.Category.SETTING)
    public static final CustomConfigOptionList WAIT_PROTO_TYPE = newConfigOptionList("waitProtoType", WaitProtoTypes.AUTO.getDefault());

    static {
        for (Field field : Configs.class.getDeclaredFields()) {
            Config annotation = field.getAnnotation(Config.class);
            if (annotation != null) {
                try {
                    Object config = field.get(null);
                    if (!(config instanceof UnlimitedTradeIConfigBase)) {
                        UnlimitedTradeMod.LOGGER.warn("{} is not subclass of AutoTradeIConfigBase", config);
                        continue;
                    }

                    CustomConfigOption option = new CustomConfigOption(annotation, (UnlimitedTradeIConfigBase) config);
                    OPTIONS.add(option);
                    TYPE_LIST_MAP.computeIfAbsent(annotation.type(), k -> new ArrayList<>()).add(option);
                    CATEGORY_TO_OPTION.computeIfAbsent(annotation.category(), k -> new ArrayList<>()).add(option);
                } catch (IllegalAccessException e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
            }
        }
    }

    static void onConfigLoaded() {
        Configs.DROP_BLOCK_LIST_RESTRICTION.setListType(UsageRestriction.ListType.BLACKLIST);
        Configs.DROP_BLOCK_LIST_RESTRICTION.setValuesForList(UsageRestriction.ListType.BLACKLIST, Configs.DROP_BLOCK_LIST.getStrings());
    }

    public static void init() {
        InitializationHandler.getInstance().registerInitializationHandler(() -> {
            ConfigManager.getInstance().registerConfigHandler(UnlimitedTradeMod.MOD_ID, CustomConfigStorage.getInstance());
            InputEventHandler.getKeybindManager().registerKeybindProvider(new KeybindProvider());

            //#if MC >= 12101
            Registry.CONFIG_SCREEN.registerConfigScreenFactory(new ModInfo(UnlimitedTradeMod.MOD_ID, UnlimitedTradeMod.MOD_NAME, CustomConfigBaseGUI::new));
            //#endif

            OPEN_CONFIG_GUI.getKeybind().setCallback(((action, key) -> {
                CustomConfigBaseGUI.openGui();
                return true;
            }));
        });
    }
}
