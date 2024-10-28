package server;

import com.google.gson.*;
import tasks.Epic;
import tasks.StatusOfTask;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EpicAdapter implements JsonSerializer<Epic>, JsonDeserializer<Epic> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public JsonElement serialize(Epic epic, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", epic.getName());
        jsonObject.addProperty("description", epic.getDescription());
        jsonObject.addProperty("id", epic.getId());
        jsonObject.addProperty("status", epic.getStatus().toString());
        jsonObject.addProperty("startTime", epic.getStartTime().format(formatter));
        jsonObject.addProperty("duration", epic.getDuration().toString());
        jsonObject.addProperty("type", epic.getType().toString());
        jsonObject.addProperty("endTime", epic.getEndTime().format(formatter));

        JsonArray subtasksArray = new JsonArray();
        ArrayList<Integer> subtasks = epic.getListOfSubtasks();
        if (subtasks.isEmpty()) {
            jsonObject.add("listOfSubtasks", new JsonArray()); // Добавляем пустой массив
        } else {
            for (Integer subtaskId : subtasks) {
                subtasksArray.add(subtaskId);
            }
            jsonObject.add("listOfSubtasks", subtasksArray);
        }

        return jsonObject;
    }

    @Override
    public Epic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        int id = jsonObject.get("id").getAsInt();
        StatusOfTask status = StatusOfTask.valueOf(jsonObject.get("status").getAsString());
        LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), formatter);
        Duration duration = Duration.parse(jsonObject.get("duration").getAsString());
        LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("endTime").getAsString(), formatter);

        ArrayList<Integer> subtasks = new ArrayList<>();
        if (jsonObject.has("listOfSubtasks")) {
            JsonArray subtasksArray = jsonObject.getAsJsonArray("listOfSubtasks");
            for (JsonElement element : subtasksArray) {
                subtasks.add(element.getAsInt());
            }
        }

        Epic epic = new Epic(name, description, id, status, startTime, (int) duration.toMinutes());
        epic.setEndTime(endTime);
        epic.setListOfSubtasks(subtasks);
        return epic;
    }
}


