package services;

import models.SimpleTask;
import models.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer server;

    @BeforeEach
    void createHttpTaskManager() throws IOException {
        server = new KVServer();
        server.start();
        taskManager = new HttpTaskManager();
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void shouldReturnTaskWhenItWasPreviouslyAdded() {
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        taskManager.createNewTask(simpleTask);
        SimpleTask loadTask = (SimpleTask) taskManager.getTaskByIdWithoutSaveInHistory(simpleTask.getId());
        assertEquals(simpleTask, loadTask);
    }
}
