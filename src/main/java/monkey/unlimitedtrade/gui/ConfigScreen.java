package monkey.unlimitedtrade.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import monkey.unlimitedtrade.AutoTradModClient;

import java.util.List;

public class ConfigScreen extends GuiConfigsBase {
    public ConfigScreen() {
        super(10, 50, AutoTradModClient.MOD_ID, null, "unlimitedtrade.gui.title", AutoTradModClient.VERSION);
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return null;
    }
}
