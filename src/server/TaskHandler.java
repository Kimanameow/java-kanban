package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.Managers;
import taskmanager.TaskManager;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

class TaskHandler extends BaseHttpHandler implements HttpHandler {


    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int id = getIdFromPath(httpExchange);
        try {
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    if (id != 0) {
                        Task t = manager.getTaskPerId(id);
                        if (t != null) {
                            sendResponse(200, httpExchange, convertJson(t));
                        } else {
                            sendResponse(404, httpExchange, "Not found");
                        }
                    } else {
                        sendResponse(200, httpExchange, convertJson(manager.allTasks()));
                    }
                    break;
                case "DELETE":
                    if (id != 0) {
                        manager.removeTaskPerId(id);
                        sendResponse(200, httpExchange, "Successful!");
                    } else {
                        manager.removeTasks();
                        sendResponse(201, httpExchange, "All tasks removed");
                    }
                    break;
                case "POST":
                    Task task = postRequestFromUser(httpExchange);
                    if (task == null) {
                        sendResponse(400, httpExchange, "Can't add this task");
                    } else {
                        if (task.getId() == 0) {
                            manager.add(task);
                        } else {
                            manager.updateTask(task.getId(), task);
                        }
                        sendResponse(201, httpExchange, "Successful");
                    }
                    break;
                default:
                    sendResponse(405, httpExchange, "Method not allowed");
            }
        } catch (NumberFormatException e) {
            sendResponse(404, httpExchange, "Invalid number format");
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
            if (path.length > 2) {
                return Integer.parseInt(path[2]);
            }
        } catch (NumberFormatException e) {
            return 0;
        }
        return 0;
    }

    public <T extends Task> T postRequestFromUser(HttpExchange h) {
        String requestBody;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(h.getRequestBody()))) {
            requestBody = br.lines().collect(Collectors.joining(System.lineSeparator()));
            if (requestBody != null && !requestBody.isEmpty()) {
                return gson.fromJson(requestBody, (Class<T>) Task.class);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}