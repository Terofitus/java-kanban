package ru.yandex.practicum.taskTracker;

import ru.yandex.practicum.taskTracker.model.*;
import ru.yandex.practicum.taskTracker.service.*;

public class Main {
    public static void main(String[] args) {
        TaskTrackerManager taskTrackerManager = new TaskTrackerManager();
        SimpleTask simpleTask = new SimpleTask("Simple Task #1","Description of Simple Task #1",
                Status.NEW);
        SimpleTask simpleTask1 = new SimpleTask("Simple Task #2", "Description of Simple Task #2",
                Status.IN_PROGRESS);
        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic1, Status.IN_PROGRESS);
        Subtask subtask1 = new Subtask("Subtask #2 of Epic #1", "Description of Subtask #2 of Epic #1",
                epic1, Status.NEW);
        Epic epic2 = new Epic("Epic #2", "Description of Epic #2");
        Subtask subtaskOfEpic2 = new Subtask("Subtask #1 of Epic #2",
                "Description of Subtask #1 of Epic #2",
                epic2, Status.NEW);
        taskTrackerManager.createNewTask(simpleTask);
        taskTrackerManager.createNewTask(simpleTask1);
        taskTrackerManager.createNewTask(epic1);
        taskTrackerManager.createNewTask(subtask);
        taskTrackerManager.createNewTask(subtask1);
        taskTrackerManager.createNewTask(epic2);
        taskTrackerManager.createNewTask(subtaskOfEpic2);
        System.out.println(taskTrackerManager.getTaskById(epic2.getId()));
        subtaskOfEpic2.setStatus(Status.DONE);
        taskTrackerManager.updateStatusOfEpic(epic2);
        System.out.println(taskTrackerManager.getTaskById(epic2.getId()));
        taskTrackerManager.printTasksAllTypeByType();
        taskTrackerManager.deleteTaskById(subtaskOfEpic2.getId());
        taskTrackerManager.printTasksByType(TypeOfTask.EPIC);
        taskTrackerManager.deleteAllTasks();
        taskTrackerManager.printTasksAllTypeByType();
    }
}

