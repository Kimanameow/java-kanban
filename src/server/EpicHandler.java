package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.EpicNotFoundException;
import tasks.Epic;

import java.io.IOException;

class EpicHandler extends TaskHandler implements HttpHandler {

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
                        throw new CantAddTaskException("Can't add this epic");
                    } else {
                        if (epic.getId() == 0) {
                            manager.add(epic);
                        } else {
                            manager.updateTask(epic.getId(), epic);
                        }
                        sendResponse(201, httpExchange, "Successful");
                    }
                case "DELETE":
                    manager.removeEpics();
                    sendResponse(201, httpExchange, "Successful");
                    break;
                default:
                    sendResponse(405, httpExchange, "Method not allowed");
            }


        } else {
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            if (path.length > 3) {
                try {
                    if (method.equals("GET")) {
                        sendResponse(200, httpExchange, convertJson(manager.subtasksForEpic(id)));
                    } else throw new IndexOutOfBoundsException("Request method not found");
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
                }
            }
        }
    }
}
