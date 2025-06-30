package me.monkeycat.unlimitedtrade.client.config;


import fi.dy.masa.malilib.interfaces.IStringValue;
import fi.dy.masa.malilib.util.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    Type type();

    Category category();

    enum Type implements IStringValue {
        HOTKEY, LIST;

        @Override
        public String getStringValue() {
            return StringUtils.translate("unlimitedtrade.gui.config_type." + this.name().toLowerCase());
        }
    }

    enum Category {
        SETTING;

        public String getDisplayName() {
            return StringUtils.translate(String.format("unlimitedtrade.config.%s.name", this.name().toLowerCase()));
        }

        public String getDescription() {
            return StringUtils.translate(String.format("unlimitedtrade.config.%s.comment", this.name().toLowerCase()));
        }
    }
}
