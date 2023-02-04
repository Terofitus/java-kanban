package ru.yandex.practicum.taskTracker.services;

public class Managers {
    private static HistoryManager historyManager;
    private static TaskManager taskManager;

    private Managers() {
    }

    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new InMemoryTaskManager();
        }
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }
}
