package ru.yandex.practicum.taskTracker.models;

import java.util.Objects;

public class Subtask extends Task {
    private final int epicID;

    public int getEpicID() {
        return epicID;
    }

    public Subtask(String name, String description, Epic epic, Status status) {
        super(name, description);
        super.setId();
        epicID = epic.getId();
        setStatus(status);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epicID=" + getEpicID() +
                '}';
    }
}
