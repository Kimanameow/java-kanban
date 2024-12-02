package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.TaskManager;
import tasks.Subtask;

import java.io.IOException;

class SubtaskHandler extends TaskHandler implements HttpHandler {

    public SubtaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String request = httpExchange.getRequestMethod();
        int id = getIdFromPath(httpExchange);
        if (id == 0) {
            switch (request) {
                case "GET":
                    sendResponse(200, httpExchange, convertJson(manager.allSubtasks()));
                    break;
                case "POST":
                    Subtask subtask = postRequestFromUser(httpExchange);
                    if (subtask == null) {
                        sendResponse(400, httpExchange, "Can't add");
                    } else {
                        if (subtask.getId() == 0) {
                            manager.add(subtask);
                        } else {
                            manager.updateSubtask(subtask.getId(), subtask);
                        }
                        sendResponse(201, httpExchange, "Successful");
                        break;
                    }
                default:
                    sendResponse(405, httpExchange, "Method not allowed");
                    break;
            }
        } else {
            switch (request) {
                case "GET":
                    sendResponse(200, httpExchange, convertJson(manager.getSubtaskPerId(id)));
                    break;
                case "DELETE":
                    manager.removeSubtaskPerId(id);
                    sendResponse(201, httpExchange, "Deleted");
                    break;
                default:
                    sendResponse(405, httpExchange, "Method not allowed");
                    break;
            }

        }
    }
}