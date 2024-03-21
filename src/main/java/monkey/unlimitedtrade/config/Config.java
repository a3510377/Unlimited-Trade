package monkey.unlimitedtrade.config;


import fi.dy.masa.malilib.util.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    Category category();

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
