package monkey.unlimitedtrade.config.types;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

import java.util.Arrays;

public interface BaseType extends IConfigOptionListEntry {
    String name();

    int ordinal();

    BaseType[] getAllValues();

    BaseType getDefault();

    String getTranslationPrefix();

    @Override
    default String getStringValue() {
        return this.name().toLowerCase();
    }

    @Override
    default String getDisplayName() {
        return StringUtils.translate(this.getTranslationPrefix() + this.getStringValue());
    }

    @Override
    default IConfigOptionListEntry cycle(boolean forward) {
        int index = this.ordinal() + (forward ? 1 : -1);
        BaseType[] values = this.getAllValues();

        return values[(index + values.length) % values.length];
    }

    @Override
    default IConfigOptionListEntry fromString(String value) {
        return Arrays.stream(getAllValues()).filter(o -> o.name().equalsIgnoreCase(value)).findFirst().orElseGet(this::getDefault);
    }
}
