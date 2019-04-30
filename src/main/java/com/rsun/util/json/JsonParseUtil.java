package com.rsun.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Objects;

public class JsonParseUtil {

    private static Gson gson;

    public static Gson getGson() {
        if (Objects.isNull(gson)) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }

    public static String convertString(Object src) {
        return getGson().toJson(src);
    }

    public static JsonObject convertJson(String str) {
        return (JsonObject) new JsonParser().parse(str);
    }
}
