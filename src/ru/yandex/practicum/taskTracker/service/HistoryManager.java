package ru.yandex.practicum.taskTracker.service;

import ru.yandex.practicum.taskTracker.model.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}