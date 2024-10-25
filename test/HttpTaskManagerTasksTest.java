import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import server.HttpTaskServer;
import server.LocalDateTimeAdapter;
import taskmanager.InMemoryTaskManager;
import taskmanager.TaskManager;
/*import tasks.StatusOfTask;
import tasks.Task;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;*/


import java.io.IOException;
/*import java.net.URI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;*/
import java.time.LocalDateTime;


public class HttpTaskManagerTasksTest {
    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = new Gson();

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
/*
    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        final List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test 1", tasks.getFirst().getName(), "Некорректное имя задачи");
    }
*/
}