package ru.yandex.practicum.taskTracker.services;

import ru.yandex.practicum.taskTracker.exceptions.ManagerLoadException;
import ru.yandex.practicum.taskTracker.exceptions.ManagerSaveException;
import ru.yandex.practicum.taskTracker.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    Path pathToDirectory = Paths.get("src" + System.getProperty("file.separator") + "resources");
    Path pathToFile = Paths.get(pathToDirectory + System.getProperty("file.separator") + "save.csv");


    public FileBackedTasksManager() {
        super();
        load();
    }

    @Override
    public ArrayList<Task> getListOfTasks(TypeOfTask type) {
        ArrayList<Task> list = super.getListOfTasks(type);
        save();
        return list;
    }

    private ArrayList<Task> getListOfTasksWithoutSave() {
        return super.getListOfTasksWithoutSaveInHistory(TypeOfTask.TASK);
    }

    @Override
    public void createNewTask(SimpleTask task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewTask(Epic epic) {
        super.createNewTask(epic);
        save();
    }

    @Override
    public void createNewTask(Subtask task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void printTasksByType(TypeOfTask type) {
        super.printTasksByType(type);
        save();
    }

    @Override
    public void updateStatusOfEpic(Epic epic) {
        super.updateStatusOfEpic(epic);
        save();
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        super.addSubtaskToEpic(subtask);
        save();
    }

    @Override
    boolean isNotEmptyMap() {
        return super.isNotEmptyMap();
    }

    private void save() throws ManagerSaveException {
        try {
            if (!Files.exists(pathToDirectory.toAbsolutePath()))
                Files.createDirectory(pathToDirectory.toAbsolutePath());
            if (!Files.exists(pathToFile.toAbsolutePath())) Files.createFile(pathToFile.toAbsolutePath());
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ощибка во время создания файла по указанному пути!");
        }
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(pathToFile.toAbsolutePath().toString(), StandardCharsets.UTF_8))) {
            bw.write("id,type,name,description,status,IDs of epic or subtasks\n");
            if (isNotEmptyMap()) {
                for (Task task : getListOfTasksWithoutSave()) {
                    bw.write(toString(task));
                }
            }
            bw.write("\n" + historyToString(Managers.getDefaultHistory()));
        } catch (IOException ioException) {
            throw new ManagerSaveException("Произошла ошибка во время сохранения TaskManager!");
        }
    }

    private String toString(Task task) {
        String info = null;
        String type;
        if (task instanceof SimpleTask) {
            type = "SimpleTask";
            info = task.getId() + "," + type + "," + task.getName() + "," + task.getDescription()
                    + "," + task.getStatus();
        } else if (task instanceof Epic) {
            type = "Epic";
            info = task.getId() + "," + type + "," + task.getName() + "," + task.getDescription()
                    + "," + task.getStatus() + "," + ((Epic) task).getListOfIdOfSubtasks();
        } else if (task instanceof Subtask) {
            type = "Subtask";
            info = task.getId() + "," + type + "," + task.getName() + "," + task.getDescription()
                    + "," + task.getStatus() + "," + ((Subtask) task).getEpicID();
        }
        return info + "\n";
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder stringOfHistory = new StringBuilder();
        for (Task task : manager.getHistory()) {
            stringOfHistory.append(task.getId()).append(" ");
        }
        if (stringOfHistory.length() > 1) {
            stringOfHistory = new StringBuilder(stringOfHistory.toString().replaceAll("\\s", ","));
            return stringOfHistory.substring(0, stringOfHistory.length() - 1);
        } else {
            return stringOfHistory.toString();
        }
    }

    private void load() throws ManagerLoadException {
        if (Files.exists(pathToFile.toAbsolutePath())) {
            try (BufferedReader br = new BufferedReader(
                    new FileReader(pathToFile.toAbsolutePath().toString(), StandardCharsets.UTF_8))) {
                String taskFromString = br.readLine();
                while (!(taskFromString = br.readLine()).equals("")) {
                    Task task = fromString(taskFromString);
                    createNewTaskToLoad(task);
                }
                for (Integer integer : historyFromString(br.readLine())) {
                    getTaskById(integer);
                }
                System.out.println("Загруженно сохраненное состояние TaskManager!");
            } catch (IOException ioException) {
                throw new ManagerLoadException("Произошла ошибка во время загрузки TaskManager!");
            }
        } else {
            try {
                if (!Files.exists(pathToDirectory.toAbsolutePath()))
                    Files.createDirectory(pathToDirectory.toAbsolutePath());
                Files.createFile(pathToFile.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createNewTaskToLoad(Task task) {
        if (task instanceof SimpleTask) {
            createNewTask((SimpleTask) task);
        } else if (task instanceof Epic) {
            createNewTask((Epic) task);
        } else if (task instanceof Subtask) {
            createNewTask((Subtask) task);
        }
    }

    private Task fromString(String value) {
        String[] splitString = value.split(",");
        String type = splitString[1];
        Status status;
        Task task;
        switch (splitString[4]) {
            case "IN_PROGRESS":
                status = Status.IN_PROGRESS;
                break;
            case "DONE":
                status = Status.DONE;
                break;
            default:
                status = Status.NEW;
        }
        switch (type) {
            case "Epic":
                Epic epic = new Epic(splitString[2], splitString[3]);
                if (splitString.length > 5) {
                    splitString[5] = splitString[5].replace("[", "")
                            .replace("]", "");
                    String[] subtasksID = splitString[5].split(";");
                    if (subtasksID.length != 0) {
                        for (String s : subtasksID) {
                            epic.addSubtaskID(Integer.parseInt(s));
                        }
                    }
                }
                task = epic;
                break;
            case "Subtask":
                task = new Subtask(splitString[2], splitString[3], Integer.parseInt(splitString[5]), status);
                break;
            default:
                task = new SimpleTask(splitString[2], splitString[3], status);
        }
        return task;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> listOfID = new ArrayList<>();
        if (value != null) {
            String[] splitedStringOfHistory = value.split(",");
            for (String s : splitedStringOfHistory) {
                listOfID.add(Integer.parseInt(s));
            }
        }
        return listOfID;
    }
}
