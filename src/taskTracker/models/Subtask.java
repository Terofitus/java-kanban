package models;

import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    private final int epicID;

    public Subtask(String name, String description, Epic epic, Status status, String startOfTask, int duration) {
        super(name, description, startOfTask, duration);
        epicID = epic.getId();
        setStatus(status);
    }

    public Subtask(String name, String description, Integer epicID, Status status, String startOfTask, int duration) {
        super(name, description, startOfTask, duration);
        this.epicID = epicID;
        setStatus(status);
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' + ", description='" + getDescription() + '\'' +
                ", id=" + getId() + ", status=" + getStatus() + ", epicID=" + getEpicID() +
                ", time of start=" + getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", duration=" + getDurationOfTask() +
                ", time of end=" + getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                '}';
    }
}
