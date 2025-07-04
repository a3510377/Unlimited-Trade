package me.monkeycat.unlimitedtrade.server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class UnlimitedTradeTranslations {
    private static final Gson GSON = new GsonBuilder().create();

    public static Map<String, String> getTranslationFromResourcePath(String lang) {
        String path = "assets/unlimitedtrade/lang/%s.json".formatted(lang);

        try (InputStream langFile = UnlimitedTradeTranslations.class.getClassLoader().getResourceAsStream(path)) {
            if (langFile == null) {
                return Collections.emptyMap();
            }

            String jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);

            return GSON.fromJson(jsonData, new TypeToken<Map<String, String>>() {
            }.getType());

        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }
}
