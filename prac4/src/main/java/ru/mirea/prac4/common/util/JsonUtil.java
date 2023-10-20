package ru.mirea.prac4.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;

public class JsonUtil {

    private final static ObjectReader jsonReader =  new ObjectMapper().reader();
    private final static ObjectWriter jsonWriter =  new ObjectMapper().writer();

    @SneakyThrows
    public static String writeJson(Object object) {
        return jsonWriter.withDefaultPrettyPrinter().writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T readJson(String json, Class<T> tClass) {
        return jsonReader.readValue(json, tClass);
    }
}
