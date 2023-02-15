package ru.yandex.practicum.taskTracker.services;

public class Managers {
    private static HistoryManager historyManager;
    private static TaskManager taskManager;

    private Managers() {
    }

    public static TaskManager getFileBackedTasksManager() {
        if (taskManager == null) {
            taskManager = new FileBackedTasksManager();
        }
        return taskManager;
    }

    public static TaskManager getInMemoryTasksManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }
}
