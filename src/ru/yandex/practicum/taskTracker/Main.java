package ru.yandex.practicum.taskTracker;

import ru.yandex.practicum.taskTracker.models.*;
import ru.yandex.practicum.taskTracker.services.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // внизу в виде закомментированного метода оставил собственные вызовы методов, по которым проверял себя
        // надеюсь, это не проблема
        TaskManager taskTrackerManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW);
        SimpleTask simpleTask1 = new SimpleTask("Simple Task #2", "Description of Simple Task #2",
                Status.IN_PROGRESS);
        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic1, Status.IN_PROGRESS);
        Subtask subtask1 = new Subtask("Subtask #2 of Epic #1", "Description of Subtask #2 of Epic #1",
                epic1, Status.NEW);
        Subtask subtaskOfEpic1 = new Subtask("Subtask #1 of Epic #2",
                "Description of Subtask #1 of Epic #2",
                epic1, Status.NEW);
        Epic epic2 = new Epic("Epic #1", "Description of Epic #1");
        taskTrackerManager.createNewTask(simpleTask);
        taskTrackerManager.createNewTask(simpleTask1);
        taskTrackerManager.createNewTask(epic1);
        taskTrackerManager.createNewTask(subtask);
        taskTrackerManager.createNewTask(subtask1);
        taskTrackerManager.createNewTask(subtaskOfEpic1);
        taskTrackerManager.createNewTask(epic2);
        taskTrackerManager.getTaskById(0);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.getTaskById(1);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.getTaskById(0);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.getTaskById(2);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.getTaskById(3);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.getTaskById(4);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.getTaskById(5);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.getTaskById(5);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.getTaskById(4);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.deleteTaskById(1);
        System.out.println(historyManager.getHistory());
        taskTrackerManager.deleteTaskById(2);
        System.out.println(historyManager.getHistory());
    }

    /*static void myPersonalTest() {
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
        System.out.println(taskTrackerManager.getTaskById(0));
        System.out.println(historyManager.getHistory());
        subtaskOfEpic2.setStatus(Status.DONE);
        taskTrackerManager.updateStatusOfEpic(epic2);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(1);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(2);
        taskTrackerManager.getTaskById(3);
        taskTrackerManager.getTaskById(4);
        taskTrackerManager.getTaskById(5);
        taskTrackerManager.getTaskById(5);
        taskTrackerManager.getTaskById(4);
        System.out.println("");
        int i = 1;
        for (Task task : historyManager.getHistory()) {
            System.out.println("Task #" + i++ + ": " + task);
        }
        System.out.println("");
        taskTrackerManager.getTaskById(1);
        taskTrackerManager.getTaskById(3);
        i = 1;
        for (Task task : historyManager.getHistory()) {
            System.out.println("Task #" + i++ + ": " + task);
        }
        System.out.println("");
        i = 1;
        taskTrackerManager.printTasksByType(TypeOfTask.TASK);
        for (Task task : historyManager.getHistory()) {
            System.out.println("Task #" + i++ + ": " + task);
        }
        System.out.println("");
        taskTrackerManager.deleteAllTasks();
        i = 1;
        for (Task task : historyManager.getHistory()) {
            System.out.println("Task #" + i++ + ": " + task);
        }
        taskTrackerManager.printTasksByType(TypeOfTask.TASK);
    }*/
}

