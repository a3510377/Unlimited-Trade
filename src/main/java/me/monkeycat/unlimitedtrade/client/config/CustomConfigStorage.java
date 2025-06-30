package me.monkeycat.unlimitedtrade.client.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.JsonUtils;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.config.options.UnlimitedTradeIConfigBase;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static me.monkeycat.unlimitedtrade.client.config.Configs.CATEGORY_TO_OPTION;

public class CustomConfigStorage implements IConfigHandler {
    public static final String CONFIG_FILE_NAME = UnlimitedTradeMod.MOD_ID + ".json";
    private static final CustomConfigStorage INSTANCE = new CustomConfigStorage();
    private JsonObject jsonObject = new JsonObject();

    public static File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME).toFile();
    }

    public static CustomConfigStorage getInstance() {
        return INSTANCE;
    }

    @Override
    public void load() {
        loadFromFile(getConfigFile());
    }

    public void loadFromFile(File configFile) {
        JsonElement element = JsonUtils.parseJsonFile(configFile);

        if (element != null && element.isJsonObject()) {
            this.jsonObject = element.getAsJsonObject();

            for (Config.Category category : Config.Category.values()) {
                List<UnlimitedTradeIConfigBase> configs = CATEGORY_TO_OPTION.get(category).stream().map(CustomConfigOption::config).collect(Collectors.toList());
                ConfigUtils.readConfigBase(jsonObject, category.name(), configs);
            }
        } else save();
        Configs.onConfigLoaded();
    }

    @Override
    public void save() {
        for (Config.Category category : Config.Category.values()) {
            List<UnlimitedTradeIConfigBase> configs = CATEGORY_TO_OPTION.get(category).stream().map(CustomConfigOption::config).collect(Collectors.toList());
            ConfigUtils.writeConfigBase(jsonObject, category.name(), configs);
        }

        File configFile = getConfigFile();
        JsonUtils.writeJsonToFile(jsonObject, configFile);
    }
}