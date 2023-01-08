package ru.yandex.practicum.taskTracker.model;

public class SimpleTask extends Task {

    public SimpleTask(String name, String description, Status status) {
        super(name, description);
        super.setId();
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
