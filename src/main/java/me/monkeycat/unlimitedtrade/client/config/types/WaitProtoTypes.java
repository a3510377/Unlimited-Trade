package me.monkeycat.unlimitedtrade.client.config.types;

import me.monkeycat.unlimitedtrade.client.config.options.CustomEnumOptionEntry;

public enum WaitProtoTypes implements CustomEnumOptionEntry {
    AUTO, UNLIMITED_TRADE, CHUNKDEBUG, DELAY;

    public CustomEnumOptionEntry getDefault() {
        return AUTO;
    }

    @Override
    public CustomEnumOptionEntry[] getAllValues() {
        return values();
    }

    @Override
    public String getTranslationName() {
        return "wait_proto_type";
    }
}
