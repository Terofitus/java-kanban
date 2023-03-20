package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.InMemoryTaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    InMemoryTaskManager taskManager;
    Epic epic;
    Subtask subtask;
    Subtask subtask1;

    @BeforeEach
    void createManagerAndTasks() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Epic #1", "Description of Epic #1");
        taskManager.createNewTask(epic);
        subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic, Status.NEW, "20.03.2023 22:22", 12);
        taskManager.createNewTask(subtask);
        subtask1 = new Subtask("Subtask #2 of Epic #1", "Description of Subtask #2 of Epic #1",
                epic, Status.NEW, "24.03.2023 22:22", 13);
        taskManager.createNewTask(subtask1);
    }

    @Test
    void shouldHaveStatusNewWhenListOfSubtasksIdIsEmpty() {
        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        Status status = epic1.getStatus();
        assertEquals(Status.NEW, status, "Статус эпика без сабтасок не NEW.");
    }

    @Test
    void shouldHaveStatusNewWhenAllSubtasksHaveStatusNew() {
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status, "Статус эпика с сабтасками со статусом NEW - не NEW.");
    }

    @Test
    void shouldHaveStatusDoneWhenAllSubtasksHaveStatusDone() {
        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.DONE, status, "Статус эпика с сабтасками со статусом DONE - не DONE.");
    }

    @Test
    void shouldHaveStatusNewWhenSubtasksHaveStatusNewAndDone() {
        subtask.setStatus(Status.NEW);
        subtask1.setStatus(Status.DONE);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status
                , "Статус эпика с сабтасками со статусом NEW и DONE - не IN_PROGRESS.");
    }

    @Test
    void shouldHaveStatusInProgressWhenSubtasksHaveStatusInProgress() {
        subtask.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epic);
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status, "Статус эпика с сабтасками со статусом"
                + "IN_PROGRESS - не IN_PROGRESS.");
    }

    @Test
    void shouldReturnListOfSubtasksId() {
        ArrayList<Integer> list = epic.getSubtasksID();
        assertEquals(2, list.size(), "У эпика не добавлены ID сабтасок.");
    }

    @Test
    void shouldReturnEpicToString() {
        assertEquals("Epic{name='Epic #1', description='Description of Epic #1', id=0, id of subtasks=1;2,"
                        + " status=NEW, time of start=20.03.2023 22:22, duration=5773, time of end=24.03.2023 22:35}",
                epic.toString(), "Строковое представление эпика не соответствует ожидаемому.");
    }

    @Test
    void shouldDeleteSubtaskId() {
        epic.deleteSubtaskID(subtask.getId());
        assertEquals(1, epic.getSubtasksID().size(), "После удаления количество подзадач не " +
                "изменилось.");
    }

    @Test
    void shouldAddSubtaskIdToEpic() {
        Subtask subtask2 = new Subtask("Subtask #3 of Epic #1", "Description of Subtask #2 of Epic #1",
                epic, Status.NEW, "18.03.2023 22:22", 13);
        taskManager.createNewTask(subtask2);
        assertEquals(3, epic.getSubtasksID().size(), "Сабтаска не была добавлена к эпику.");
    }
}
