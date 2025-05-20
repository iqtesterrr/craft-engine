package net.momirealms.craftengine.core.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GsonHelper {
    private final Gson gson;

    public GsonHelper() {
        this.gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
    }

    public Gson getGson() {
        return gson;
    }

    public static Gson get() {
        return SingletonHolder.INSTANCE.getGson();
    }

    private static class SingletonHolder {
        private static final GsonHelper INSTANCE = new GsonHelper();
    }

    public static void writeJsonFile(JsonElement json, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            get().toJson(json, writer);
        }
    }

    public static JsonElement readJsonFile(Path path) throws IOException, JsonSyntaxException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader);
        }
    }

    public static JsonObject shallowMerge(JsonObject obj1, JsonObject obj2) {
        JsonObject merged = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : obj1.entrySet()) {
            merged.add(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, JsonElement> entry : obj2.entrySet()) {
            merged.add(entry.getKey(), entry.getValue());
        }
        return merged;
    }

    public static JsonObject deepMerge(JsonObject source, JsonObject target) {
        JsonObject merged = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
            merged.add(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, JsonElement> entry : target.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (merged.has(key)) {
                JsonElement existingValue = merged.get(key);
                if (existingValue.isJsonObject() && value.isJsonObject()) {
                    JsonObject mergedChild = deepMerge(
                            existingValue.getAsJsonObject(),
                            value.getAsJsonObject()
                    );
                    merged.add(key, mergedChild);
                } else {
                    merged.add(key, value);
                }
            } else {
                merged.add(key, value);
            }
        }
        return merged;
    }

    public static JsonObject parseJsonToJsonObject(String json) {
        try {
            return get().fromJson(
                    json,
                    JsonObject.class
            );
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Invalid JSON response: " + json, e);
        }
    }

    public static Map<String, Object> parseJsonToMap(String json) {
        try {
            return GsonHelper.get().fromJson(
                    json,
                    new TypeToken<Map<String, Object>>() {}.getType()
            );
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Invalid JSON response: " + json, e);
        }
    }

    public static JsonElement combine(List<? extends JsonElement> jo) {
        if (jo.size() == 1) {
            return jo.get(0);
        } else {
            JsonArray ja = new JsonArray();
            for (JsonElement je : jo) {
                ja.add(je);
            }
            return ja;
        }
    }
}
