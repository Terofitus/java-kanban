package ru.yandex.practicum.taskTracker.model;

import ru.yandex.practicum.taskTracker.service.Status;

public abstract class Task {
    private final String name;
    private final String description;
    private Integer id;
    private Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
