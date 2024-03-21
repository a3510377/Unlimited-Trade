package monkey.unlimitedtrade.gui.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import monkey.unlimitedtrade.gui.AutoTradGuiConfigsBase;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            AutoTradGuiConfigsBase gui = new AutoTradGuiConfigsBase();
            gui.setParent(screen);
            return gui;
        };
    }
}
