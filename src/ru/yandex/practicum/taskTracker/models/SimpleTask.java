package ru.yandex.practicum.taskTracker.models;

public class SimpleTask extends Task {

    public SimpleTask(String name, String description, Status status) {
        super(name, description);
        setStatus(status);
    }

    @Override
    public String toString() {
        return "SimpleTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }
}
