package services;

import models.Epic;
import models.SimpleTask;
import models.Status;
import models.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager;
    SimpleTask task;

    @BeforeEach
    void createNewHistoryManager() {
        historyManager = new InMemoryHistoryManager();
        task = new SimpleTask("s", "a", Status.NEW
                , "20.03.2023 22:22", 10);
    }

    @Test
    void shouldAddTaskWhenAddTaskInListOfHistoryThatIsEmpty() {
        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task), "В списке истории нет добавленной задачи.");
    }

    @Test
    void shouldAddTaskWhenAddTasKThatIsAlreadyAdded() {
        historyManager.add(task);
        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task), "В списке истории нет добавленной задачи.");
    }

    @Test
    void shouldGetListOfHistoryWhenTasksWereAdded() {
        historyManager.add(task);
        assertFalse(historyManager.getHistory().isEmpty(), "Список истории пуст.");
    }

    @Test
    void shouldGetEmptyListOfHistoryWhenTasksWereNotAdded() {
        assertTrue(historyManager.getHistory().isEmpty(), "Список истории не пуст.");
    }

    @Test
    void shouldReturnTrueWhenRemoved() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createNewTask(task);
        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task), "В списке истории нет добавленной задачи.");
        boolean isRemoved = historyManager.remove(task.getId());
        assertTrue(isRemoved, "Задача не была удалена.");
        assertFalse(historyManager.getHistory().contains(task), "Удаленная задача есть в списке истории.");
    }

    @Test
    void shouldRemoveAllTasksFromHistory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createNewTask(task);
        historyManager.add(task);
        assertFalse(historyManager.getHistory().isEmpty(), "История не пуста после удаления всех задач.");
    }

    @Test
    void shouldReturnFalseWhenTryToRemoveFromEmptyListOfHistory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createNewTask(task);
        boolean isRemoved = historyManager.remove(task.getId());
        assertFalse(isRemoved, "Метод удаления вернул true.");
    }

    @Test
    void shouldRemoveFromBeginningMiddleAndEndOfListOfHistory() {
        TaskManager taskManager = Managers.getInMemoryTasksManager();
        taskManager.createNewTask(task);
        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic1);
        SimpleTask simpleTask1 = new SimpleTask("Simple Task #2", "Description of Simple Task #2",
                Status.IN_PROGRESS, "22.03.2023 22:22", 1);
        taskManager.createNewTask(simpleTask1);
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic1, Status.IN_PROGRESS, "20.03.2023 11:22", 12);
        taskManager.createNewTask(subtask);
        Subtask subtask1 = new Subtask("Subtask #2 of Epic #1", "Description of Subtask #2 of Epic #1",
                epic1, Status.NEW, "24.03.2023 22:22", 13);
        taskManager.createNewTask(subtask1);
        taskManager.getListOfTasks(TypeOfTask.TASK);
        InMemoryHistoryManager history = (InMemoryHistoryManager) Managers.getDefaultHistory();
        assertEquals(5, history.getHistory().size(), "В списке истории не 5 записей.");

        history.remove(task.getId());
        history.remove(simpleTask1.getId());
        history.remove(subtask1.getId());
        assertEquals(2, history.getHistory().size(), "В списке истории не 3 записи.");
    }
}