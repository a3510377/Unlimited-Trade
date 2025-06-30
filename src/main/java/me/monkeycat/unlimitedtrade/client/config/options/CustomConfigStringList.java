package me.monkeycat.unlimitedtrade.client.config.options;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigStringList;

public class CustomConfigStringList extends ConfigStringList implements UnlimitedTradeIConfigBase {
    public CustomConfigStringList(String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, CUSTOM_NAMESPACE_PREFIX + name + COMMENT_SUFFIX, CUSTOM_NAMESPACE_PREFIX + name + PRETTY_NAME_SUFFIX);
    }
}
