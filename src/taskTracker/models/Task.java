package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class Task {
    private static Integer nextID = 0;
    private final String name;
    private final String description;
    private Integer id;
    private Status status;
    private LocalDateTime startTime;
    private Integer durationOfTask;

    protected Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected Task(String name, String description, String startOfTask, int duration) {
        this.name = name;
        this.description = description;
        startTime = LocalDateTime.parse(startOfTask, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        durationOfTask = duration;
    }

    public static void setNewId(Task task) {
        task.id = nextID++;
    }

    public static void resetCounterOfId() {
        nextID = 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
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

    public void setOldId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    protected void setStartTime(LocalDateTime time) {
        startTime = time;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(durationOfTask);
    }

    public Integer getDurationOfTask() {
        return durationOfTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description);
    }
}
