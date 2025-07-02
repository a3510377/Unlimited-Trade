package me.monkeycat.unlimitedtrade.common.network;

public record UnlimitedTradeHelloPayload(int version) {
    public static final UnlimitedTradeHelloPayload INSTANCE = new UnlimitedTradeHelloPayload(1_0_0);
}
