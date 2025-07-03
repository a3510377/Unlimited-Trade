package me.monkeycat.unlimitedtrade.client.config.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.config.Config;
import me.monkeycat.unlimitedtrade.client.config.Configs;
import me.monkeycat.unlimitedtrade.client.config.CustomConfigOption;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CustomConfigBaseGUI extends GuiConfigsBase {
    private final String tab;

    public CustomConfigBaseGUI() {
        this(Config.Category.SETTING.name());
    }

    public CustomConfigBaseGUI(String defaultTab) {
        super(10, 50, UnlimitedTradeMod.MOD_ID, null, "unlimitedtrade.gui.title", UnlimitedTradeMod.VERSION);

        tab = defaultTab;
    }

    public static void openGui() {
        GuiBase.openGui(new CustomConfigBaseGUI());
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return Configs.CATEGORY_TO_OPTION
                .getOrDefault(Config.Category.valueOf(tab), List.of()).stream()
                .map(CustomConfigOption::config)
                .sorted(Comparator.comparing(IConfigBase::getName, String.CASE_INSENSITIVE_ORDER))
                .map(ConfigOptionWrapper::new)
                .collect(Collectors.toList());
    }
}
