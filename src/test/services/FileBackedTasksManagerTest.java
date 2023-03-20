package services;

import models.Epic;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @BeforeEach
    void createFileBackedTasksManager() {
        taskManager = new FileBackedTasksManager(false, false);
    }

    @Test
    void shouldSaveManagerWithoutTasksAndHistoryWhenTasksWereNotAdded() {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
        taskManager = new FileBackedTasksManager(false, true);
        historyManager.deleteHistory();
        taskManager.getTaskById(5);
        taskManager = new FileBackedTasksManager(true, false);
        boolean isEmptyListOfTasks = taskManager.getListOfTasks(TypeOfTask.TASK).isEmpty();
        boolean isEmptyHistory = historyManager.getHistory().isEmpty();
        assertTrue(isEmptyHistory, "Список истории не пуст.");
        assertTrue(isEmptyListOfTasks, "Список менеджера не пустой, когда задачи не были добавлены.");
    }

    @Test
    void shouldSaveAndLoadEpicWithoutSubtasks() {
        taskManager = new FileBackedTasksManager(false, true);
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        taskManager = new FileBackedTasksManager(true, false);
        Task task = taskManager.getTaskById(epic.getId());
        assertEquals(epic, task, "Эпик не был загружен из файла.");
    }
}
