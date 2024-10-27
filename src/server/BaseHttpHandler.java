package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import history.HistoryManager;
import taskmanager.Managers;
import taskmanager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    TaskManager manager = Managers.getDefault();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    HistoryManager historyManager = Managers.getDefaultHistory();

    protected void sendResponse(int responseCode, HttpExchange exchange, String arguments) throws IOException {
        byte[] text = arguments.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, text.length);
        exchange.getResponseBody().write(text);
        exchange.close();
    }
}
