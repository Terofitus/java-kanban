package models;

import java.time.format.DateTimeFormatter;

public class SimpleTask extends Task {

    public SimpleTask(String name, String description, Status status, String startOfTask, int duration) {
        super(name, description, startOfTask, duration);
        setStatus(status);
    }

    @Override
    public String toString() {
        return "SimpleTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", time of start=" + getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", duration=" + getDurationOfTask() +
                ", time of end=" + getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                '}';
    }
}
