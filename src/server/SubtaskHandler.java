package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.NotAvailableTimeException;
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
                case "POST":
                    try {
                        Subtask subtask = postRequestFromUser(httpExchange);
                        if (subtask.getId() == 0) {
                            manager.add(subtask);
                        } else {
                            manager.updateSubtask(subtask.getId(), subtask);
                        }
                        sendResponse(201, httpExchange, "Successful");
                    } catch (NotAvailableTimeException n) {
                        sendResponse(406, httpExchange, "Not acceptable");
                    }
                default:
                    throw new RequestMethodException("Bad request");
            }
        } else {
            switch (request) {
                case "GET":
                    try {
                        sendResponse(200, httpExchange, convertJson(manager.getSubtaskPerId(id)));
                        break;
                    } catch (NullPointerException n) {
                        sendResponse(404, httpExchange, "Not found");
                    }
                case "DELETE":
                    manager.removeSubtaskPerId(id);
                    sendResponse(201, httpExchange, "Deleted");
                    break;
                default:
                    throw new RequestMethodException("Bad request");
            }

        }
    }
}