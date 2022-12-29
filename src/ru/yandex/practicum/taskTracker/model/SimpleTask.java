package ru.yandex.practicum.taskTracker.model;

import ru.yandex.practicum.taskTracker.service.Status;

public class SimpleTask extends Task {

    public SimpleTask(String name, String description, Status status) {
        super(name, description);
        super.setStatus(status);
    }

    public void setStatus(Status status) {
        super.setStatus(status);
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
