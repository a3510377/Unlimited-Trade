package me.monkeycat.unlimitedtrade.client.config;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;

import java.util.List;

public class KeybindProvider implements IKeybindProvider {
    private static final List<IHotkey> ALL_CUSTOM_HOTKEYS = Configs.OPTIONS.stream().map(CustomConfigOption::config).filter(option -> option instanceof IHotkey).map(option -> (IHotkey) option).toList();

    @Override
    public void addKeysToMap(IKeybindManager iKeybindManager) {
        ALL_CUSTOM_HOTKEYS.forEach(iHotkey -> iKeybindManager.addKeybindToMap(iHotkey.getKeybind()));
    }

    @Override
    public void addHotkeys(IKeybindManager iKeybindManager) {
        iKeybindManager.addHotkeysForCategory(UnlimitedTradeMod.MOD_ID, UnlimitedTradeMod.MOD_ID + ".hotkeys.category.main", ALL_CUSTOM_HOTKEYS);
    }
}