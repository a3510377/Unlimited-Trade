package me.monkeycat.unlimitedtrade.server;

import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.FEATURE;

public class UnlimitedTradeModSettings {
    public static final String TRADE = "trade";

    @Rule(categories = {FEATURE, TRADE})
    public static boolean disableEndGatewayAnyTicket = false;

    @Rule(categories = {FEATURE, TRADE})
    public static boolean disableEndGatewayPlayerTicket = false;

    @Rule(categories = {FEATURE, TRADE})
    public static boolean enablePersistentTradingScreen = false;
}
