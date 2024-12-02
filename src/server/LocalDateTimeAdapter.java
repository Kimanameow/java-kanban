package server;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss.SSSSSS");

    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateTime.format(formatter));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}
