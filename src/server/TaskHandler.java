package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.TaskManager;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

class TaskHandler extends BaseHttpHandler implements HttpHandler {
    protected final TaskManager manager;
    Gson gson = new Gson();

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int id = getIdFromPath(httpExchange);
        try {
            if (id != 0) {
                switch (httpExchange.getRequestMethod()) {
                    case "GET":
                        Task t = manager.getTaskPerId(id);
                        if (t != null) {
                            sendResponse(200, httpExchange, convertJson(t));
                            break;
                        } else {
                            sendResponse(404, httpExchange, "Not found");
                            break;
                        }
                    case "DELETE":
                        manager.removeTaskPerId(id);
                        sendResponse(200, httpExchange, "Successful!");
                        break;
                    default:
                        throw new RequestMethodException("Request method not found");
                }
            }
        } catch (NumberFormatException e) {
            sendResponse(404, httpExchange, "Invalid number format");
        }
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                manager.allTasks();
                sendResponse(200, httpExchange, convertJson(manager.allTasks()));
                break;
            case "DELETE":
                manager.removeTasks();
                sendResponse(201, httpExchange, "Successful");
                break;
            case "POST":
                Task task = postRequestFromUser(httpExchange);
                try {
                    if (task.getId() == 0) {
                        manager.add(task);
                    } else {
                        manager.updateTask(task.getId(), task);
                    }
                    sendResponse(201, httpExchange, "Successful");
                } catch (NullPointerException n) {
                    sendResponse(404, httpExchange, "Can't resolve task");
                }
            default:
                throw new RequestMethodException("Request method not found");
        }
    }

    public String convertJson(Object o) {
        if (o == null) {
            return "{}";
        }
        return gson.toJson(o);
    }

    public int getIdFromPath(HttpExchange httpExchange) {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        try {
            int id = Integer.parseInt(path[2]);
            return id;
        } catch (NullPointerException n) {
            return 0;
        }
    }

    public <T extends Task> T postRequestFromUser(HttpExchange h) throws IOException {
        String requestBody;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(h.getRequestBody()))) {
            requestBody = br.lines().collect(Collectors.joining(System.lineSeparator()));
            T task = gson.fromJson(requestBody, (Class<T>) Task.class);
            return task;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
