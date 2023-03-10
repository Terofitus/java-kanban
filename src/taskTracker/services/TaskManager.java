package services;

import models.Epic;
import models.SimpleTask;
import models.Subtask;
import models.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getListOfTasks(TypeOfTask type);

    void createNewTask(SimpleTask task);

    void createNewTask(Epic epic);

    void createNewTask(Subtask task);

    void deleteAllTasks();

    Task getTaskById(Integer id);

    void deleteTaskById(int id);

    void printTasksByType(TypeOfTask type);

    void updateEpic(Epic epic);

    void addSubtaskToEpic(Subtask subtask);
}
