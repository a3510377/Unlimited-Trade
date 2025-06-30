package me.monkeycat.unlimitedtrade.client.config;

import me.monkeycat.unlimitedtrade.client.config.options.UnlimitedTradeIConfigBase;

public record CustomConfigOption(Config annotation, UnlimitedTradeIConfigBase config) {
    public Config.Type getType() {
        return annotation.type();
    }

    public Config.Category getCategory() {
        return annotation.category();
    }

    public UnlimitedTradeIConfigBase getConfig() {
        return config;
    }
}
