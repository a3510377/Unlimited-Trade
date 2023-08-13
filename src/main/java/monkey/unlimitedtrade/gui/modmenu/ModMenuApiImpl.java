package monkey.unlimitedtrade.gui.modmenu;

import monkey.unlimitedtrade.AutoTradModClient;
import monkey.unlimitedtrade.gui.ConfigScreen;
import top.hendrixshen.magiclib.compat.modmenu.ModMenuCompatApi;

public class ModMenuApiImpl implements ModMenuCompatApi {
    @Override
    public ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat() {
        return screen -> {
            ConfigScreen configScreen = ConfigScreen.instance;

            configScreen.setParent(screen);

            return configScreen;
        };
    }

    @Override
    public String getModIdCompat() {
        return AutoTradModClient.MOD_ID;
    }
}
