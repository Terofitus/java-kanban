package ru.yandex.practicum.taskTracker.services;

public class Managers {
    private static HistoryManager historyManager;
    private static TaskManager taskManager;

    private Managers() {
    }

    public static TaskManager getDefault() {
        //если сначала не инициализирую объект InMemoryHistoryManager, то при инициализации InMemoryTaskManager
        // он не будет иметь ссылку на историю вызовов, что ее обновлять
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
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
