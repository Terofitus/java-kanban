package ru.yandex.practicum.taskTracker;

import ru.yandex.practicum.taskTracker.model.*;
import ru.yandex.practicum.taskTracker.service.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskTrackerManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
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
        System.out.println(historyManager.getHistory());
        subtaskOfEpic2.setStatus(Status.DONE);
        taskTrackerManager.updateStatusOfEpic(epic2);
        taskTrackerManager.printTasksByType(TypeOfTask.TASK);
        taskTrackerManager.printTasksByType(TypeOfTask.EPIC);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(1);
        taskTrackerManager.getTaskById(2);
        taskTrackerManager.getTaskById(3);
        taskTrackerManager.getTaskById(4);
        taskTrackerManager.getTaskById(5);
        taskTrackerManager.getTaskById(5);
        taskTrackerManager.getTaskById(5);
        taskTrackerManager.getTaskById(4);
        System.out.println("");
        List<Task> historyOfTasks = historyManager.getHistory();
        int i = 0;
        for (Task task: historyOfTasks) {
            System.out.println("Task #" + i++ + ": " + task);
        }
        System.out.println("");
        i = 0;
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        for (Task task: historyOfTasks) {
            System.out.println("Task #" + i++ + ": " + task);
        }
        taskTrackerManager.deleteAllTasks();
        taskTrackerManager.printTasksByType(TypeOfTask.TASK);
    }
}

