package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import history.HistoryManager;
import taskmanager.EpicNotFoundException;
import taskmanager.TaskManager;
import tasks.Epic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

class EpicHandler extends TaskHandler implements HttpHandler {

    public EpicHandler(TaskManager manager, HistoryManager historyManager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int id = getIdFromPath(httpExchange);
        if (id == 0) {
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    sendResponse(200, httpExchange, convertJson(manager.allEpics()));
                    break;
                case "POST":
                    String requestBody;
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()))) {
                        requestBody = br.lines().collect(Collectors.joining(System.lineSeparator()));
                        if (requestBody == null || requestBody.isEmpty()) {
                            sendResponse(400, httpExchange, "Request body is empty");
                            break;
                        }
                        Epic epic = gson.fromJson(requestBody, Epic.class);
                        if (epic == null) {
                            sendResponse(405, httpExchange, "Can't read epic");
                            break;
                        } else {
                            if (epic.getId() == 0) {
                                manager.add(epic);
                            } else {
                                manager.updateEpic(epic.getId(), epic);
                            }
                            sendResponse(201, httpExchange, "Successful");
                            break;
                        }
                    }
                case "DELETE":
                    manager.removeEpics();
                    sendResponse(201, httpExchange, "Successful");
                    break;
                default:
                    sendResponse(405, httpExchange, "Method not allowed");
                    break;
            }


        } else {
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            if (path.length > 4) {
                try {
                    if (httpExchange.getRequestMethod().equals("GET") || path[3].equals("subtasks")) {
                        sendResponse(200, httpExchange, convertJson(manager.subtasksForEpic(id)));
                    } else sendResponse(405, httpExchange, "Method not allowed");
                } catch (EpicNotFoundException e) {
                    sendResponse(404, httpExchange, "Epic not found!");
                }
            } else {
                switch (httpExchange.getRequestMethod()) {
                    case "GET":
                        sendResponse(200, httpExchange, convertJson(manager.getEpicPerId(id)));
                        break;
                    case "DELETE":
                        sendResponse(201, httpExchange, "Successful");
                        break;
                    default:
                        sendResponse(405, httpExchange, "Method not allowed");
                        break;
                }
            }
        }
    }
}
