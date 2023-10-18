package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest {

    @Test
    void shouldGetFileBackedTasksManager() {
        TaskManager taskManager = Managers.getFileBackedTasksManager();
        assertEquals(FileBackedTasksManager.class, taskManager.getClass()
                , "Менеджер вернул не FileBackedTasksManager.");
    }

    @Test
    void shouldGetInMemoryTasksManager() {
        TaskManager taskManager = Managers.getInMemoryTasksManager();
        assertEquals(InMemoryTaskManager.class, taskManager.getClass()
                , "Менеджер вернул не InMemoryTaskManager.");
    }

    @Test
    void shouldGetDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertEquals(InMemoryHistoryManager.class, historyManager.getClass()
                , "Менеджер вернул не InMemoryHistoryManager.");
    }
}