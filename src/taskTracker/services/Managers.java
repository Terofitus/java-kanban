package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.Task;
import server.KVClient;
import server.LocalDateTimeAdapter;
import server.ArrayListOfSortedTasksAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(type, new ArrayListOfSortedTasksAdapter())
                    .create();
        }
        return gson;
    }
}
