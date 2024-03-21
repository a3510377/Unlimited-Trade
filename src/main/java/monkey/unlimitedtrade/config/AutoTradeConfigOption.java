package monkey.unlimitedtrade.config;

import monkey.unlimitedtrade.config.options.AutoTradeIConfigBase;

public record AutoTradeConfigOption(Config annotation, AutoTradeIConfigBase config) {

    public Config.Category getCategory() {
        return this.annotation.category();
    }
}
