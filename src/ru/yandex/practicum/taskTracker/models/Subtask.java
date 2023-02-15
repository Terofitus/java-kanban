package ru.yandex.practicum.taskTracker.models;

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

    public Subtask(String name, String description, Integer epicID, Status status) {
        super(name, description);
        super.setId();
        this.epicID = epicID;
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
