package me.monkeycat.unlimitedtrade.client.config.options;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;

public class CustomConfigBoolHotkey extends ConfigBooleanHotkeyed implements UnlimitedTradeIConfigBase {
    public CustomConfigBoolHotkey(String name, Boolean defaultValue, String defaultHotKey) {
        super(name, defaultValue, defaultHotKey, CUSTOM_NAMESPACE_PREFIX + name + COMMENT_SUFFIX, CUSTOM_NAMESPACE_PREFIX + name + PRETTY_NAME_SUFFIX);
    }
}
