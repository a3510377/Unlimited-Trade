package me.monkeycat.unlimitedtrade.client.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import me.monkeycat.unlimitedtrade.client.config.options.CustomConfigBoolHotkey;
import me.monkeycat.unlimitedtrade.client.config.options.CustomConfigHotkey;
import me.monkeycat.unlimitedtrade.client.config.options.CustomConfigOptionList;
import me.monkeycat.unlimitedtrade.client.config.options.CustomConfigStringList;

public abstract class ConfigFactory {
    public static CustomConfigHotkey newConfigHotKey(String name, String defaultStorageString) {
        return new CustomConfigHotkey(name, defaultStorageString);
    }

    public static CustomConfigStringList newConfigStringList(String name, ImmutableList<String> defaultValue) {
        return new CustomConfigStringList(name, defaultValue);
    }

    public static CustomConfigOptionList newConfigOptionList(String name, IConfigOptionListEntry defaultValue) {
        return new CustomConfigOptionList(name, defaultValue);
    }

    public static CustomConfigBoolHotkey newConfigBoolHotkey(String name, Boolean defaultValue, String defaultHotKey) {
        return new CustomConfigBoolHotkey(name, defaultValue, defaultHotKey);
    }
}
