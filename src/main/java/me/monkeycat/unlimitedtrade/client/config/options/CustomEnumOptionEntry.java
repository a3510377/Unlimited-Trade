package me.monkeycat.unlimitedtrade.client.config.options;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

import java.util.Arrays;

public interface CustomEnumOptionEntry extends IConfigOptionListEntry {
    CustomEnumOptionEntry getDefault();

    String name();

    int ordinal();

    CustomEnumOptionEntry[] getAllValues();

    String getTranslationName();

    @Override
    default String getStringValue() {
        return this.name().toLowerCase();
    }

    @Override
    default String getDisplayName() {
        return StringUtils.translate(UnlimitedTradeIConfigBase.CUSTOM_NAMESPACE_PREFIX + this.getTranslationName() + "." + this.name().toLowerCase());
    }

    @Override
    default IConfigOptionListEntry cycle(boolean forward) {
        int index = this.ordinal() + (forward ? 1 : -1);
        CustomEnumOptionEntry[] values = this.getAllValues();

        return values[(index + values.length) % values.length];
    }

    @Override
    default IConfigOptionListEntry fromString(String value) {
        return Arrays.stream(getAllValues()).filter(o -> o.name().equalsIgnoreCase(value)).findFirst().orElseGet(this::getDefault);
    }
}
