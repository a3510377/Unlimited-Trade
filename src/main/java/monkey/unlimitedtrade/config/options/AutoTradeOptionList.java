package monkey.unlimitedtrade.config.options;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigOptionList;

public class AutoTradeOptionList extends ConfigOptionList implements AutoTradeIConfigBase {

    public AutoTradeOptionList(String name, IConfigOptionListEntry defaultValue) {
        super(name, defaultValue,
                AUTOTRADE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX,
                AUTOTRADE_NAMESPACE_PREFIX + name + PRETTY_NAME_SUFFIX);
    }
}
