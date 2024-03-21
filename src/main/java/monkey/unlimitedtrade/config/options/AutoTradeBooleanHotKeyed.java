/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * TweakerMore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TweakerMore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TweakerMore.  If not, see <https://www.gnu.org/licenses/>.
 */

package monkey.unlimitedtrade.config.options;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;

public class AutoTradeBooleanHotKeyed extends ConfigBooleanHotkeyed implements AutoTradeIConfigBase {
    public AutoTradeBooleanHotKeyed(String name) {
        this(name, false);
    }

    public AutoTradeBooleanHotKeyed(String name, String defaultHotkey) {
        this(name, false, defaultHotkey);
    }

    public AutoTradeBooleanHotKeyed(String name, boolean defaultValue) {
        this(name, defaultValue, "");
    }

    public AutoTradeBooleanHotKeyed(String name, boolean defaultValue, String defaultHotkey) {
        super(name, defaultValue, defaultHotkey,
                AUTOTRADE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX,
                AUTOTRADE_NAMESPACE_PREFIX + name + PRETTY_NAME_SUFFIX
        );
    }
}
