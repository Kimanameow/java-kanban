package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.TaskManager;

import java.io.IOException;

class PrioritizedHandler extends TaskHandler implements HttpHandler {
    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            sendResponse(200, httpExchange, convertJson(manager.getPrioritizedTasks()));
        } else throw new RequestMethodException("Request method not found");
    }
}
