package me.monkeycat.unlimitedtrade.client.config.options;

import fi.dy.masa.malilib.config.options.ConfigHotkey;

public class CustomConfigHotkey extends ConfigHotkey implements UnlimitedTradeIConfigBase {
    public CustomConfigHotkey(String name, String defaultStorageString) {
        super(name, defaultStorageString, CUSTOM_NAMESPACE_PREFIX + name + COMMENT_SUFFIX, CUSTOM_NAMESPACE_PREFIX + name + PRETTY_NAME_SUFFIX);
    }
}
