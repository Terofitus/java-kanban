package ru.yandex.practicum.taskTracker.services;

import ru.yandex.practicum.taskTracker.models.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
