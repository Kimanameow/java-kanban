import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;//
import server.HttpTaskServer;
import server.LocalDateTimeAdapter;
import taskmanager.InMemoryTaskManager;
import taskmanager.TaskManager;
//import tasks.StatusOfTask;//
//import tasks.Task;//


import java.io.IOException;
/*import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;*/
import java.time.LocalDateTime;

/*import static org.junit.Assert.assertEquals;
import static server.HttpHandler.gson;*/


public class HttpTaskManagerTasksTest {
    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer server = new HttpTaskServer(manager);

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void startServer() {
        server.startServer();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        Gson gson = gsonBuilder.create();
    }

    @AfterEach
    public void stopServer() {
        server.stopServer();
    }

    /*@Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(task);

        int taskId = task.getId();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(url)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

     */

}