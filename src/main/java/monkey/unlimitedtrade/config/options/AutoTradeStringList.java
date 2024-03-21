package monkey.unlimitedtrade.config.options;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigStringList;

public class AutoTradeStringList extends ConfigStringList implements AutoTradeIConfigBase {
    public AutoTradeStringList(String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, AUTOTRADE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }
}
