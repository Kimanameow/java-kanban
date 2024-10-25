package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import history.HistoryManager;
import taskmanager.Managers;
import taskmanager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    TaskManager manager = Managers.getDefault();
    Gson gson = new Gson();
    HistoryManager historyManager = Managers.getDefaultHistory();

    protected void sendResponse(int responseCode, HttpExchange exchange, String arguments) throws IOException {
        byte[] text = arguments.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, text.length);
        exchange.getResponseBody().write(text);
        exchange.close();
    }
}
