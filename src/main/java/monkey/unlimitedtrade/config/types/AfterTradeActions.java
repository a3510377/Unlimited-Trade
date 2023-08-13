package monkey.unlimitedtrade.config.types;

public enum AfterTradeActions implements BaseType {
    USE, USE_AND_DROP, FILL_CONTAINER;

    public static final AfterTradeActions DEFAULT = USE_AND_DROP;

    @Override
    public BaseType[] getAllValues() {
        return values();
    }

    @Override
    public BaseType getDefault() {
        return DEFAULT;
    }

    @Override
    public String getTranslationPrefix() {
        return "unlimitedtrade.label.afterTradeActions.";
    }
}
