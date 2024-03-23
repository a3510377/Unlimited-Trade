package monkey.unlimitedtrade.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.JsonUtils;
import monkey.unlimitedtrade.AutoTradeModClient;
import monkey.unlimitedtrade.config.options.AutoTradeIConfigBase;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static monkey.unlimitedtrade.config.Configs.CATEGORY_TO_OPTION;

public class AutoTradeConfigStorage implements IConfigHandler {
    public static final String CONFIG_FILE_NAME = AutoTradeModClient.MOD_ID + ".json";
    private static final AutoTradeConfigStorage INSTANCE = new AutoTradeConfigStorage();
    private JsonObject jsonObject = new JsonObject();

    public static File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME).toFile();
    }

    public static AutoTradeConfigStorage getInstance() {
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
                List<AutoTradeIConfigBase> configs = CATEGORY_TO_OPTION.get(category).stream()
                        .map(AutoTradeConfigOption::config).collect(Collectors.toList());
                ConfigUtils.readConfigBase(jsonObject, category.name(), configs);
            }
        } else save();
    }

    @Override
    public void save() {
        for (Config.Category category : Config.Category.values()) {
            List<AutoTradeIConfigBase> configs = CATEGORY_TO_OPTION.get(category).stream()
                    .map(AutoTradeConfigOption::config).collect(Collectors.toList());
            ConfigUtils.writeConfigBase(jsonObject, category.name(), configs);
        }

        File configFile = getConfigFile();
        JsonUtils.writeJsonToFile(jsonObject, configFile);
    }
}
