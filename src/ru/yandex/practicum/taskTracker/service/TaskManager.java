package ru.yandex.practicum.taskTracker.service;

import ru.yandex.practicum.taskTracker.model.Epic;
import ru.yandex.practicum.taskTracker.model.SimpleTask;
import ru.yandex.practicum.taskTracker.model.Subtask;
import ru.yandex.practicum.taskTracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList getListOfTasks(TypeOfTask type);

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
