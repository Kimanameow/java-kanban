package server;

import com.sun.net.httpserver.HttpServer;
import history.HistoryManager;
import taskmanager.Managers;
import taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;

    public HttpTaskServer(int port) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        httpServer.createContext("/task", new TaskHandler(manager));
        httpServer.createContext("/subtask", new SubtaskHandler(manager));
        httpServer.createContext("/epic", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(historyManager));
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

    public static void main(String[] args) {
        int port = 8080;
        try {
            HttpTaskServer server = new HttpTaskServer(port);
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}