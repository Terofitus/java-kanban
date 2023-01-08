package ru.yandex.practicum.taskTracker.model;

public abstract class Task {
    private final String name;
    private final String description;
    private Integer id;
    private Status status;
    private static Integer nextID = 0;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(){
        id = nextID++;
    }
}
