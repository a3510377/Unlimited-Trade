package monkey.unlimitedtrade.config;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import monkey.unlimitedtrade.AutoTradeModClient;

import java.util.List;

public class KeybindProvider implements IKeybindProvider {
    private static final List<IHotkey> ALL_CUSTOM_HOTKEYS = Configs.OPTIONS.stream().map(AutoTradeConfigOption::config)
            .filter(option -> option instanceof IHotkey).map(option -> (IHotkey) option).toList();

    @Override
    public void addKeysToMap(IKeybindManager iKeybindManager) {
        ALL_CUSTOM_HOTKEYS.forEach(iHotkey -> iKeybindManager.addKeybindToMap(iHotkey.getKeybind()));
    }

    @Override
    public void addHotkeys(IKeybindManager iKeybindManager) {
        iKeybindManager.addHotkeysForCategory(AutoTradeModClient.MOD_ID, "tweakermore.hotkeys.category.main", ALL_CUSTOM_HOTKEYS);
    }
}
