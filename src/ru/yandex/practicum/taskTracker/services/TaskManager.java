package ru.yandex.practicum.taskTracker.services;

import ru.yandex.practicum.taskTracker.models.Epic;
import ru.yandex.practicum.taskTracker.models.SimpleTask;
import ru.yandex.practicum.taskTracker.models.Subtask;
import ru.yandex.practicum.taskTracker.models.Task;

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

    void updateStatusOfEpic(Epic epic);

    void addSubtaskToEpic(Subtask subtask);
}
