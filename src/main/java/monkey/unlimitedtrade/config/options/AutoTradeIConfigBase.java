package monkey.unlimitedtrade.config.options;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import monkey.unlimitedtrade.AutoTradeModClient;

public interface AutoTradeIConfigBase extends IConfigBase {
    String AUTOTRADE_NAMESPACE_PREFIX = AutoTradeModClient.MOD_ID + ".config.";
    String COMMENT_SUFFIX = ".comment";
    String PRETTY_NAME_SUFFIX = ".pretty_name";

    @Override
    default String getComment() {
        return StringUtils.getTranslatedOrFallback(AUTOTRADE_NAMESPACE_PREFIX + this.getName() + COMMENT_SUFFIX,
                this.getName());
    }

    @Override
    default String getPrettyName() {
        return StringUtils.getTranslatedOrFallback(AUTOTRADE_NAMESPACE_PREFIX + this.getName() + PRETTY_NAME_SUFFIX,
                this.getConfigGuiDisplayName());
    }

    @Override
    default String getConfigGuiDisplayName() {
        return StringUtils.getTranslatedOrFallback(AUTOTRADE_NAMESPACE_PREFIX + this.getName() + ".name",
                this.getName());
    }
}
