package services;

import com.google.gson.Gson;
import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import models.Epic;
import models.SimpleTask;
import models.Subtask;
import models.Task;
import server.KVClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVClient client;
    private final Gson gson;

    public HttpTaskManager() {
        super(false, true);
        client = Managers.getKVClient();
        gson = Managers.getGson();
        load();
    }

    @Override
    void save() throws ManagerSaveException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        String jsonHistory = gson.toJson(historyManager.getHistory());

        List<Task> simpleTasks = getListOfTasksWithoutSaveInHistory(TypeOfTask.SIMPLE_TASK);
        List<Task> subTasks = getListOfTasksWithoutSaveInHistory(TypeOfTask.SUBTASK);
        List<Task> epicTasks = getListOfTasksWithoutSaveInHistory(TypeOfTask.EPIC);

        String jsonSimpleTasks = gson.toJson(simpleTasks);
        String jsonSubtasks = gson.toJson(subTasks);
        String jsonEpics = gson.toJson(epicTasks);

        try {
            client.put("history", jsonHistory);
            client.put("simpleTask", jsonSimpleTasks);
            client.put("subtask", jsonSubtasks);
            client.put("epic", jsonEpics);
        } catch (IOException e) {
            System.out.println("Во время сохранения произошло IOException");
        } catch (InterruptedException e) {
            System.out.println("Во время сохранения произошло InterruptedException");
        }
    }

    @Override
    void load() throws ManagerLoadException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        String listOfSimpleTasks = null;
        String listOfEpics = null;
        String listOfSubtasks = null;
        String listOfHistory = null;
        try {
            listOfSimpleTasks = client.load("simpleTask");
            listOfEpics = client.load("epic");
            listOfSubtasks = client.load("subtask");
            listOfHistory = client.load("history");
        } catch (IOException e) {
            System.out.println("Во время загрузки произошло IOException");
        } catch (InterruptedException e) {
            System.out.println("Во время загрузки произошло InterruptedException");
        }

        var simpleTasks = gson.fromJson(listOfSimpleTasks, ArrayList.class);
        var epicTasks = gson.fromJson(listOfEpics, ArrayList.class);
        var subTasks = gson.fromJson(listOfSubtasks, ArrayList.class);
        var history = gson.fromJson(listOfHistory, ArrayList.class);

        if (simpleTasks != null) {
            simpleTasks.forEach(task -> createNewTask((SimpleTask) task));
        }
        if (epicTasks != null) {
            epicTasks.forEach(task -> createNewTask((Epic) task));
        }
        if (subTasks != null) {
            subTasks.forEach(task -> createNewTask((Subtask) task));
        }
        if (history != null) {
            history.stream().forEach(task -> Managers.getDefaultHistory().add((Task) task));
        }
    }
}

