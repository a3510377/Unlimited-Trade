package me.monkeycat.unlimitedtrade.server;

import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.FEATURE;

public class UnlimitedTradeModSettings {
    public static final String TRADE = "trade";

    @Rule(categories = {FEATURE, TRADE})
    public static boolean enableUnlimitedTradeProtocol = true;

    //#if MC >= 12100
    //$$ @Rule(categories = {FEATURE, TRADE})
    //$$ public static boolean disableEndGatewayAnyTicket = false;
    //$$
    //$$ @Rule(categories = {FEATURE, TRADE})
    //$$ public static boolean disableEndGatewayPlayerTicket = false;
    //#endif

    //#if MC >= 12104
    //$$ @Rule(categories = {FEATURE, TRADE})
    //$$ public static boolean enablePersistentTradingScreen = false;
    //#endif
}
