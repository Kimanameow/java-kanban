package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import history.HistoryManager;

import java.io.IOException;

class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    HistoryManager manager;
    Gson gson;

    public HistoryHandler(HistoryManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                sendResponse(200, httpExchange, gson.toJson(manager.getHistory()));
            }
        } catch (IndexOutOfBoundsException e) {
            sendResponse(404, httpExchange, "Request not found");
        }
    }
}