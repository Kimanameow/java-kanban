import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import taskmanager.InMemoryTaskManager;

import java.io.IOException;


public class HttpTaskManagerTasksTest {
    HttpTaskServer server = new HttpTaskServer(8080);
    /*private InMemoryTaskManager manager;
    Gson gson;*/

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void startServer() {
        server.startServer();
    }

    @AfterEach
    public void stopServer() {
        server.stopServer();
    }
/*
    @Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(task);
        String jsonTask = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .header("Accept", "application/json")
                .uri(url)
                .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

 */
}