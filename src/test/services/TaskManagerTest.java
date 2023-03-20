package services;

import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    @Test
    void shouldReturnListOfTask() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        taskManager.createNewTask(subtask);
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        taskManager.createNewTask(simpleTask);
        List<Task> list = taskManager.getListOfTasks(TypeOfTask.TASK);
        assertEquals(ArrayList.class, list.getClass(), "Метод возврата списка вернул не List");
    }

    @Test
    void shouldReturnEmptyListOfTaskWhenTasksWereNotAdded() {
        boolean isEmpty = taskManager.getListOfTasks(TypeOfTask.TASK).isEmpty();
        assertTrue(isEmpty, "Список задач не пуст.");
    }

    @Test
    void shouldReturnTrueWhenCreateSimpleTask() {
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        boolean isCreated = taskManager.createNewTask(simpleTask);
        assertTrue(isCreated, "Простая задача не была создана.");
    }

    @Test
    void shouldReturnFalseWhenTryToCreateTaskThatWereAdded() {
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        taskManager.createNewTask(simpleTask);
        boolean isCreated = taskManager.createNewTask(simpleTask);
        assertFalse(isCreated, "Простая задача, которая уже была создана, была вновь создана.");
    }

    @Test
    void shouldReturnTrueWhenCreateEpic() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        boolean isCreated = taskManager.createNewTask(epic);
        assertTrue(isCreated, "Эпик не был создан.");
    }

    @Test
    void shouldReturnTrueWhenCreateSubtask() {
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                1, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        boolean isCreated = taskManager.createNewTask(subtask);
        assertTrue(isCreated, "Подзадача не была создана.");
    }

    @Test
    void shouldDeleteAllTasks() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        taskManager.createNewTask(subtask);
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        taskManager.createNewTask(simpleTask);
        taskManager.deleteAllTasks();
        boolean isEmpty = taskManager.getListOfTasks(TypeOfTask.TASK).isEmpty();
        assertTrue(isEmpty, "Список задач, при удалении всех задач, не пуст.");
    }

    @Test
    void shouldReturnTaskWhenIdIsCorrect() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        Epic receivedEpic = (Epic) taskManager.getTaskById(epic.getId());
        assertEquals(epic, receivedEpic, "Менеджер задач по id не вернул задачу.");
    }

    @Test
    void shouldReturnNullTWhenIdIsIncorrect() {
        Task task = taskManager.getTaskById(5);
        assertNull(task, "Переданный объект по некорректному id не равен null.");
    }

    @Test
    void shouldDeleteTaskByIdWhenGivenCorrectId() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        assertEquals(1, taskManager.getListOfTasks(TypeOfTask.TASK).size(), "В менеджере содержится" +
                " не 1 объект.");
        taskManager.deleteTaskById(epic.getId());
        boolean isContains = taskManager.getListOfTasks(TypeOfTask.TASK).contains(epic);
        assertFalse(isContains, "Эпик хранится в списке задач после удаления.");
    }

    @Test
    void shouldNotDeleteTaskWhenGivenIncorrectId() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        assertEquals(1, taskManager.getListOfTasks(TypeOfTask.TASK).size(), "В менеджере содержится" +
                " не 1 объект.");
        taskManager.deleteTaskById(5);
        boolean isContains = taskManager.getListOfTasks(TypeOfTask.TASK).contains(epic);
        assertTrue(isContains, "Эпик не хранится в списке задач.");
    }

    @Test
    void shouldUpdateStatusOfEpicWhenSubtaskStatusChange() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        assertEquals(Status.NEW, epic.getStatus(), "У пустого эпика статус не NEW.");
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic, Status.NEW, "20.03.2023 22:22", 12);
        taskManager.createNewTask(subtask);
        subtask.setStatus(Status.DONE);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.DONE, status, "Статус эпика не изменился.");
    }

    @Test
    void shouldHaveEpicIdWhenSubtasksCreated() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic, Status.NEW, "20.03.2023 22:22", 12);
        taskManager.createNewTask(subtask);
        Integer integer = subtask.getEpicID();
        assertEquals(epic.getId(), integer, "У сабтаска нет id эпика.");
    }

    @Test
    void shouldReturnSortedTreeSetWithSimpleAndSubtasks() {
        Epic epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic, Status.NEW, "20.03.2023 22:22", 12);
        taskManager.createNewTask(subtask);
        int sizeOfTreeSet = taskManager.getPrioritizedTasks().size();
        assertEquals(1, sizeOfTreeSet, "ТаскМенеджер вернул сортированное множество с " +
                "размером больше 1.");
    }
}
