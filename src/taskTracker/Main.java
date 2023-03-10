import models.*;
import services.FileBackedTasksManager;
import services.HistoryManager;
import services.Managers;
import services.TaskManager;

public class Main {
    public static void main(String[] args) {
        secondTest();
    }

    static void firstTest() {
        TaskManager taskTrackerManager = Managers.getFileBackedTasksManager();
        HistoryManager historyManager = Managers.getDefaultHistory();
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "22.02.2023 22:22", 100);
        taskTrackerManager.createNewTask(simpleTask);
        SimpleTask simpleTask1 = new SimpleTask("Simple Task #2", "Description of Simple Task #2",
                Status.IN_PROGRESS, "23.02.2023 22:22", 1100);
        taskTrackerManager.createNewTask(simpleTask1);
        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        taskTrackerManager.createNewTask(epic1);
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic1, Status.IN_PROGRESS, "24.02.2023 22:22", 12000);
        taskTrackerManager.createNewTask(subtask);
        Subtask subtask1 = new Subtask("Subtask #2 of Epic #1", "Description of Subtask #2 of Epic #1",
                epic1, Status.NEW, "25.02.2023 22:22", 12);
        taskTrackerManager.createNewTask(subtask1);
        Subtask subtaskOfEpic1 = new Subtask("Subtask #1 of Epic #2",
                "Description of Subtask #1 of Epic #2",
                epic1, Status.NEW, "26.02.2023 22:22", 11111);
        taskTrackerManager.createNewTask(subtaskOfEpic1);
        Epic epic2 = new Epic("Epic #2", "Description of Epic #1");
        taskTrackerManager.createNewTask(epic2);
        taskTrackerManager.getTaskById(6);
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

    static void secondTest() {
        TaskManager taskTrackerManager = Managers.getFileBackedTasksManager();
        HistoryManager historyManager = Managers.getDefaultHistory();
        SimpleTask simpleTask = new SimpleTask("Simple Task #1", "Description of Simple Task #1",
                Status.NEW, "23.03.2023 22:22", 1200);
        taskTrackerManager.createNewTask(simpleTask);
        SimpleTask simpleTask1 = new SimpleTask("Simple Task #2", "Description of Simple Task #2",
                Status.IN_PROGRESS, "22.03.2023 22:22", 1);
        taskTrackerManager.createNewTask(simpleTask1);
        Epic epic1 = new Epic("Epic #1", "Description of Epic #1");
        taskTrackerManager.createNewTask(epic1);
        Subtask subtask = new Subtask("Subtask #1 of Epic #1", "Description of Subtask #1 of Epic #1",
                epic1, Status.IN_PROGRESS, "20.03.2023 22:22", 12);
        taskTrackerManager.createNewTask(subtask);
        Subtask subtask1 = new Subtask("Subtask #2 of Epic #1", "Description of Subtask #2 of Epic #1",
                epic1, Status.NEW, "24.03.2023 22:22", 13);
        taskTrackerManager.createNewTask(subtask1);
        Epic epic2 = new Epic("Epic #2", "Description of Epic #2");
        taskTrackerManager.createNewTask(epic2);
        Subtask subtaskOfEpic2 = new Subtask("Subtask #1 of Epic #2",
                "Description of Subtask #1 of Epic #2",
                epic2, Status.NEW, "26.03.2023 22:22", 16);
        taskTrackerManager.createNewTask(subtaskOfEpic2);
        System.out.println(taskTrackerManager.getTaskById(0));
        System.out.println(historyManager.getHistory());
        subtaskOfEpic2.setStatus(Status.DONE);
        taskTrackerManager.updateEpic(epic2);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(1);
        taskTrackerManager.getTaskById(0);
        taskTrackerManager.getTaskById(2);
        taskTrackerManager.getTaskById(3);
        taskTrackerManager.getTaskById(4);
        taskTrackerManager.getTaskById(5);
        taskTrackerManager.getTaskById(4);
        System.out.println("");
        int i = 1;
        taskTrackerManager.getTaskById(2);
        taskTrackerManager.getTaskById(6);
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
        for (Task task : historyManager.getHistory()) {
            System.out.println("Task #" + i++ + ": " + task);
        }
        System.out.println("");
        i = 1;
        for (Task task : historyManager.getHistory()) {
            System.out.println("Task #" + i++ + ": " + task);
        }
        System.out.println(FileBackedTasksManager.historyToString(historyManager));
    }
}

