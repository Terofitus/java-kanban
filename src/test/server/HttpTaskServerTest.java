package server;

import com.google.gson.Gson;
import models.Epic;
import models.SimpleTask;
import models.Status;
import models.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    HttpTaskServer server;
    HttpClient client;
    Gson gson;
    URI uri;

    @BeforeEach
    void createHttpServer() throws IOException {
        server = new HttpTaskServer(false, false);
        client = HttpClient.newHttpClient();
        gson = Managers.getGson();
        uri = URI.create("http://localhost:8080/");
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void shouldAddTaskWhenCallEndpointTasksEpicWithMethodPost() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        String jsonEpic = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(201, responseCode);
    }

    @Test
    void shouldAddTaskWhenCallEndpointTasksTaskWithMethodPost() throws IOException, InterruptedException {
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        String jsonSimpleTask = gson.toJson(simpleTask);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSimpleTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(201, responseCode);
    }

    @Test
    void shouldAddTaskWhenCallEndpointTasksSubtaskWithMethodPost() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                2, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        String jsonSubtask = gson.toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(201, responseCode);
    }

    @Test
    void shouldReturnAllTasksWhenCallEndpointTasksTaskWithMethodGet() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                2, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        String jsonSubtask = gson.toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        String jsonSimpleTask = gson.toJson(simpleTask);
        HttpRequest request1 = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSimpleTask)).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .GET().build();
        HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        var listOfTask = gson.fromJson(response.body(), ArrayList.class);
        assertEquals(2, listOfTask.size());
    }

    @Test
    void shouldReturnTaskInJsonByIdWhenCallEndpointTasksTaskWithMethodGet() throws IOException, InterruptedException {
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        String jsonSimpleTask = gson.toJson(simpleTask);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSimpleTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(201, responseCode);

        HttpRequest request2 = HttpRequest.newBuilder(uri.resolve("/tasks/task/?id=0"))
                .GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        SimpleTask task = gson.fromJson(response2.body(), SimpleTask.class);
        assertEquals(simpleTask, task);
        assertEquals(200, response2.statusCode());
    }

    @Test
    void shouldDeleteTaskByIdWhenCallEndpointTasksTaskWithMethodDelete() throws IOException, InterruptedException {
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        String jsonSimpleTask = gson.toJson(simpleTask);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSimpleTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(201, responseCode);


        HttpRequest request1 = HttpRequest.newBuilder(uri.resolve("/tasks/task/?id=0"))
                .DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        int responseCode1 = response1.statusCode();
        assertEquals(200, responseCode1);

        HttpRequest request2 = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        var listOfTask = gson.fromJson(response2.body(), ArrayList.class);
        assertEquals(0, listOfTask.size());

        HttpRequest request3 = HttpRequest.newBuilder(uri.resolve("/tasks/task/?id=0"))
                .GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response3.statusCode());
    }

    @Test
    void shouldDeleteAllTasksWhenCallEndpointTasksTaskWithMethodDelete() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                2, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        String jsonSubtask = gson.toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        String jsonSimpleTask = gson.toJson(simpleTask);
        HttpRequest request1 = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSimpleTask)).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        int responseCode1 = response2.statusCode();
        assertEquals(200, responseCode1);

        HttpRequest request3 = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        var listOfTask = gson.fromJson(response3.body(), ArrayList.class);
        assertEquals(0, listOfTask.size());
    }

    @Test
    void shouldReturnListOfIdOfSubtasksWhenCallEndpointTasksEpicWithMethodGet() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        String jsonEpic = gson.toJson(epic1);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask1 = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                0, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        String jsonSubtask = gson.toJson(subtask1);
        HttpRequest request1 = HttpRequest.newBuilder(uri.resolve("/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask)).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        Subtask subtask2 = new Subtask("Subtask #2 of Epic #1", "Description of Subtask #1 of Epic #1",
                0, Status.IN_PROGRESS, "21.03.2023 22:22", 12);
        String jsonSubtask1 = gson.toJson(subtask2);
        HttpRequest request2 = HttpRequest.newBuilder(uri.resolve("/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask1)).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest request3 = HttpRequest.newBuilder(uri.resolve("/tasks/epic/?subtasksIdOfEpic=0"))
                .GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        var listOfTask = gson.fromJson(response3.body(), ArrayList.class);
        assertEquals(2, listOfTask.size());
    }

    @Test
    void shouldReturnIdOfEpicWhenCallEndpointTasksSubtaskWithMethodGet() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        String jsonEpic = gson.toJson(epic1);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask1 = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                0, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        String jsonSubtask = gson.toJson(subtask1);
        HttpRequest request1 = HttpRequest.newBuilder(uri.resolve("/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask)).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request3 = HttpRequest.newBuilder(uri.resolve("/tasks/subtask/?epicIdOfSubtask=1"))
                .GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        String idOfEpic = gson.fromJson(response3.body(), String.class);
        assertEquals(0, Integer.parseInt(idOfEpic));
    }

    @Test
    void shouldReturnListOfHistoryWhenCallEndpointTasksHistoryWithMethodGet() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        String jsonEpic = gson.toJson(epic1);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask1 = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                0, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        String jsonSubtask = gson.toJson(subtask1);
        HttpRequest request1 = HttpRequest.newBuilder(uri.resolve("/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask)).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        Subtask subtask2 = new Subtask("Subtask #2 of Epic #1", "Description of Subtask #1 of Epic #1",
                0, Status.IN_PROGRESS, "21.03.2023 22:22", 12);
        String jsonSubtask1 = gson.toJson(subtask2);
        HttpRequest request2 = HttpRequest.newBuilder(uri.resolve("/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask1)).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest request3 = HttpRequest.newBuilder(uri.resolve("/tasks/task/?id=1"))
                .GET().build();
        HttpResponse response6 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        HttpRequest request4 = HttpRequest.newBuilder(uri.resolve("/tasks/history"))
                .GET().build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        var listOfTask = gson.fromJson(response4.body(), ArrayList.class);
        assertEquals(1, listOfTask.size());
    }
/*    @Test
    void shouldReturnListOfPrioritizedTasksWhenCallEndpointTasksWithMethodGet() throws IOException, InterruptedException {
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        String jsonSimpleTask = gson.toJson(simpleTask);
        HttpRequest request = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSimpleTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        SimpleTask simpleTask1 = new SimpleTask("Simple Task #2", "Description of Simple Task #2",
                Status.NEW, "24.03.2023 22:22", 1200);
        String jsonSimpleTask1 = gson.toJson(simpleTask1);
        HttpRequest request1 = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSimpleTask1)).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        SimpleTask simpleTask2 = new SimpleTask("Simple Task #3", "Description of Simple Task #3",
                Status.NEW, "25.03.2023 22:22", 1200);
        String jsonSimpleTask2 = gson.toJson(simpleTask2);
        HttpRequest request2 = HttpRequest.newBuilder(uri.resolve("/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSimpleTask2)).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        HttpRequest request4 = HttpRequest.newBuilder(uri.resolve("/tasks"))
                .GET().build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> list = gson.fromJson(response4.body(), type);

        HttpRequest request5 = HttpRequest.newBuilder(uri.resolve("/tasks/task/?id=0"))
                .GET().build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        var task1 = gson.fromJson(response5.body(), SimpleTask.class);
        HttpRequest request6 = HttpRequest.newBuilder(uri.resolve("/tasks/task/?id=2"))
                .GET().build();
        HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
        var task2 = gson.fromJson(response6.body(), SimpleTask.class);

        assertEquals(task1, list.get(0));
        assertEquals(task2, list.get(2));
    }*/
}