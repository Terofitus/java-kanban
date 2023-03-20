package services;

import models.Epic;
import models.SimpleTask;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.TreeSet;

public interface TaskManager {

    ArrayList<Task> getListOfTasks(TypeOfTask type);

    boolean createNewTask(SimpleTask task);

    boolean createNewTask(Epic epic);

    boolean createNewTask(Subtask task);

    void deleteAllTasks();

    Task getTaskById(Integer id);

    void deleteTaskById(int id);

    void updateEpic(Epic epic);

    TreeSet<Task> getPrioritizedTasks();
}
