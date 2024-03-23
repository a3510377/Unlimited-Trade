package monkey.unlimitedtrade.gui;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import monkey.unlimitedtrade.AutoTradeModClient;
import monkey.unlimitedtrade.config.AutoTradeConfigOption;
import monkey.unlimitedtrade.config.Config;
import monkey.unlimitedtrade.config.Configs;

import java.util.List;

public class AutoTradGuiConfigsBase extends GuiConfigsBase {
    private final String tab;

    public AutoTradGuiConfigsBase() {
        this(Config.Category.SETTING.name());
    }

    public AutoTradGuiConfigsBase(String defaultTab) {
        super(10, 50, AutoTradeModClient.MOD_ID, null, "unlimitedtrade.gui.title", AutoTradeModClient.VERSION);

        tab = defaultTab;
    }

    public static void openGui() {
        GuiBase.openGui(new AutoTradGuiConfigsBase());
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {

        List<IConfigBase> configs = Lists.newArrayList();
        for (AutoTradeConfigOption option : Configs.CATEGORY_TO_OPTION.get(Config.Category.valueOf(tab))) {
            configs.add(option.config());
        }

        configs.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return ConfigOptionWrapper.createFor(configs);
    }
}
