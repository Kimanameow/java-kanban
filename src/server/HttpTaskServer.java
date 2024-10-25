package server;

import com.sun.net.httpserver.HttpServer;
import taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private TaskManager manager;
    static final int port = 8080;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        this.manager = manager;
        createContexts();
    }

    private void createContexts() {

        httpServer.createContext("/task", new TaskHandler());
        httpServer.createContext("/subtask", new SubtaskHandler());
        httpServer.createContext("/epic", new EpicHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
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