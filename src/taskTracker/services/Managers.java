package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.KVClient;
import server.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    private static HistoryManager historyManager;
    private static FileBackedTasksManager fileBackedTasksManager;
    private static HttpTaskManager httpManager;
    private static Gson gson;

    private Managers() {
    }

    public static HttpTaskManager getDefault() {
        if (httpManager == null) {
            httpManager = new HttpTaskManager();
        }
        return httpManager;
    }

    public static KVClient getKVClient() {
        KVClient client = null;
        try {
            client = new KVClient("http://localhost:8078/");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static FileBackedTasksManager getFileBackedTasksManager() {
        if (fileBackedTasksManager == null) {
            fileBackedTasksManager = new FileBackedTasksManager(true, true);
        }
        return fileBackedTasksManager;
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

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        }
        return gson;
    }
}
