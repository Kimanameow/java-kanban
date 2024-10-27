import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import server.DurationAdapter;
import server.HttpTaskServer;
import server.LocalDateTimeAdapter;
import taskmanager.InMemoryTaskManager;
import taskmanager.TaskManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskManagerTasksTest {
    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

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

    @Test
    public void testHistory() throws IOException, InterruptedException {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(task);
        manager.getTaskPerId(task.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotNull(response);
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testGetAllSubtasks() throws Exception {
        Epic epic1 = new Epic("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(epic1);
        Subtask stask = new Subtask("Name", "descr", 10, StatusOfTask.NEW, epic1.getId(), 10, LocalDateTime.now());
        manager.add(stask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasktask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }


    @Test
    public void testAddTask() throws Exception {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        String json = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(201, response.statusCode());
        assertEquals("Successful", response.body());
    }

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        Epic epic2 = new Epic("Name", "descr", 15, StatusOfTask.NEW, LocalDateTime.now().plusDays(1), 10);
        manager.add(epic1);
        manager.add(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testSortedMap() throws IOException, InterruptedException {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Successful!", response.body());
    }

    @Test
    public void testDeleteEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testDeleteSubtaskById() throws Exception {
        Epic epic1 = new Epic("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(epic1);
        Subtask stask = new Subtask("Name", "descr", 100, StatusOfTask.NEW, epic1.getId(), 10, LocalDateTime.now());
        manager.add(stask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/100");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }


    @Test
    public void testGetSubtasksForEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(epic1);
        int epicId = epic1.getId();

        URI url = URI.create("http://localhost:8080/epic/" + epicId + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }
}
/*
    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic");
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals("Successful", response.body());
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(epic1);
        int epicId = epic1.getId();

        URI url = URI.create("http://localhost:8080/epic/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testPostEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("New Epic", "Description", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        URI url = URI.create("http://localhost:8080/epic");

        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testGetSubtaskById() throws Exception {
        Epic epic1 = new Epic("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.add(epic1);
        Subtask stask = new Subtask("Name", "descr", 100, StatusOfTask.NEW, epic1.getId(), 10, LocalDateTime.now());
        manager.add(stask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/100");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }
}*/