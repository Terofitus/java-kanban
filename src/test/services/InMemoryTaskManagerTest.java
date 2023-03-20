package services;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void createInMemoryTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
}
