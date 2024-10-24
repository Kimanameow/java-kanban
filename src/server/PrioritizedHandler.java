package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

class PrioritizedHandler extends TaskHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            sendResponse(200, httpExchange, convertJson(manager.getPrioritizedTasks()));
        } else {
            sendResponse(405, httpExchange, "Method not allowed");
        }
    }
}
