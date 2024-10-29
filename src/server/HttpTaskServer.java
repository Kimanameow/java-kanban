package server;

import com.sun.net.httpserver.HttpServer;
import taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final TaskManager manager;
    static final int PORT = 8080;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.manager = manager;
        createContexts();
    }

    private void createContexts() {
        httpServer.createContext("/task", new TaskHandler(manager));
        httpServer.createContext("/subtask", new SubtaskHandler(manager));
        httpServer.createContext("/epic", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void startServer() {
        httpServer.start();
        System.out.println("Server started on port " + httpServer.getAddress().getPort());
    }

    public void stopServer() {
        httpServer.stop(0);
        System.out.println("Server stopped.");
    }
}