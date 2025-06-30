package me.monkeycat.unlimitedtrade.client.config.options;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigOptionList;

public class CustomConfigOptionList extends ConfigOptionList implements UnlimitedTradeIConfigBase {
    public CustomConfigOptionList(String name, IConfigOptionListEntry defaultValue) {
        super(name, defaultValue, CUSTOM_NAMESPACE_PREFIX + name + COMMENT_SUFFIX, CUSTOM_NAMESPACE_PREFIX + name + PRETTY_NAME_SUFFIX);
    }
}
