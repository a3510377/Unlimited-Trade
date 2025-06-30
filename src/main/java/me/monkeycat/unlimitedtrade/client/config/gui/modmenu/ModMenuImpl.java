package me.monkeycat.unlimitedtrade.client.config.gui.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.monkeycat.unlimitedtrade.client.config.gui.CustomConfigBaseGUI;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            CustomConfigBaseGUI gui = new CustomConfigBaseGUI();
            gui.setParent(screen);
            return gui;
        };
    }
}
