package me.monkeycat.unlimitedtrade.client.config.types;

import me.monkeycat.unlimitedtrade.client.config.options.CustomEnumOptionEntry;

public enum AfterTradeActions implements CustomEnumOptionEntry {
    OFF, USE, USE_AND_DROP;

    public CustomEnumOptionEntry getDefault() {
        return USE_AND_DROP;
    }

    @Override
    public CustomEnumOptionEntry[] getAllValues() {
        return values();
    }

    @Override
    public String getTranslationName() {
        return "after_trade_actions";
    }
}
