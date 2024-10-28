package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import history.HistoryManager;
import taskmanager.EpicNotFoundException;
import taskmanager.TaskManager;
import tasks.Epic;

import java.io.IOException;

class EpicHandler extends TaskHandler implements HttpHandler {

    public EpicHandler(TaskManager manager, HistoryManager historyManager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        int id = getIdFromPath(httpExchange);
        if (id == 0) {
            switch (method) {
                case "GET":
                    sendResponse(200, httpExchange, convertJson(manager.allEpics()));
                    break;
                case "POST":
                    Epic epic = postRequestFromUser(httpExchange);
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
                    if (method.equals("GET") || path[3].equals("subtasks")) {
                        sendResponse(200, httpExchange, convertJson(manager.subtasksForEpic(id)));
                    } else sendResponse(405, httpExchange, "Method not allowed");
                } catch (EpicNotFoundException e) {
                    sendResponse(404, httpExchange, "Epic not found!");
                }
            } else {
                switch (method) {
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
