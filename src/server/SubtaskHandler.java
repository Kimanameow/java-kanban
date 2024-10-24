package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Subtask;

import java.io.IOException;

class SubtaskHandler extends TaskHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String request = httpExchange.getRequestMethod();
        int id = getIdFromPath(httpExchange);
        if (id == 0) {
            switch (request) {
                case "GET":
                    sendResponse(200, httpExchange, convertJson(manager.allSubtasks()));
                case "POST":
                    Subtask subtask = postRequestFromUser(httpExchange);
                    if (subtask == null) {
                        throw new CantAddTaskException("Can't add Subtask");
                    } else {
                        if (subtask.getId() == 0) {
                            manager.add(subtask);
                        } else {
                            manager.updateSubtask(subtask.getId(), subtask);
                        }
                        sendResponse(201, httpExchange, "Successful");
                    }
                default:
                    sendResponse(405, httpExchange, "Method not allowed");
            }
        } else {
            switch (request) {
                case "GET":
                    sendResponse(200, httpExchange, convertJson(manager.getSubtaskPerId(id)));
                case "DELETE":
                    manager.removeSubtaskPerId(id);
                    sendResponse(201, httpExchange, "Deleted");
                    break;
                default:
                    sendResponse(405, httpExchange, "Method not allowed");
            }

        }
    }
}