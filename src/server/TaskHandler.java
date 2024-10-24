package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

class TaskHandler extends BaseHttpHandler implements HttpHandler {

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
                        } else {
                            sendResponse(404, httpExchange, "Not found");
                        }
                        break;
                    case "DELETE":
                        manager.removeTaskPerId(id);
                        sendResponse(200, httpExchange, "Successful!");
                        break;
                    default:
                        sendResponse(405, httpExchange, "Method not allowed");
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
                if (task == null) {
                    throw new CantAddTaskException("Can't add this task");
                } else {
                    if (task.getId() == 0) {
                        manager.add(task);
                    } else {
                        manager.updateTask(task.getId(), task);
                    }
                    sendResponse(201, httpExchange, "Successful");
                }
            default:
                sendResponse(405, httpExchange, "Method not allowed");
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
        String id = path[2];
        if (id == null) {
            return 0;
        } else return Integer.parseInt(id);
    }

    public <T extends Task> T postRequestFromUser(HttpExchange h) throws IOException {
        String requestBody;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(h.getRequestBody()))) {
            requestBody = br.lines().collect(Collectors.joining(System.lineSeparator()));
            T task = gson.fromJson(requestBody, (Class<T>) Task.class);
            return task;
        }
    }
}
