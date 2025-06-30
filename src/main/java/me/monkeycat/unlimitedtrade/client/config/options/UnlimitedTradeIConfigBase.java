package me.monkeycat.unlimitedtrade.client.config.options;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;

public interface UnlimitedTradeIConfigBase extends IConfigBase {
    String CUSTOM_NAMESPACE_PREFIX = UnlimitedTradeMod.MOD_ID + ".config.";
    String COMMENT_SUFFIX = ".comment";
    String PRETTY_NAME_SUFFIX = ".pretty_name";

    @Override
    default String getConfigGuiDisplayName() {
        return StringUtils.translate(CUSTOM_NAMESPACE_PREFIX + this.getName() + ".name");
    }
}
