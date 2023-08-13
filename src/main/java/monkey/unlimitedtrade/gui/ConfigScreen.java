package monkey.unlimitedtrade.gui;


import fi.dy.masa.malilib.util.StringUtils;
import monkey.unlimitedtrade.AutoTradModClient;
import monkey.unlimitedtrade.config.Configs;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.gui.ConfigGui;

public class ConfigScreen extends ConfigGui {
    public static final ConfigScreen instance = new ConfigScreen(
        AutoTradModClient.MOD_ID,
        Configs.Category.SETTING,
        ConfigManager.get(AutoTradModClient.MOD_ID)
    );

    private ConfigScreen(String identifier, String defaultTab, ConfigManager configManager) {
        super(identifier, defaultTab, configManager, () -> StringUtils.translate("unlimitedtrade.gui.title", AutoTradModClient.VERSION));
    }
}
