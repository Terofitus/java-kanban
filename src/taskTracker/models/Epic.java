package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksID;
    private LocalDateTime endOfEpic;

    public Epic(String name, String description) {
        super(name, description);
        subtasksID = new ArrayList<>();
        setStatus(Status.NEW);
    }

    public ArrayList<Integer> getSubtasksID() {
        return subtasksID;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", id of subtasks=" + getListOfIdOfSubtasks() +
                ", status=" + getStatus() +
                ", time of start=" + (getStartTime() != null ? getStartTime()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : null) +
                ", duration=" + (getDurationOfTask() != null ? getDurationOfTask() : null) +
                ", time of end=" + (getEndTime() != null ? getEndTime()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : null) +
                '}';
    }

    public String getListOfIdOfSubtasks() {
        if (!subtasksID.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            subtasksID.forEach(id -> sb.append(id).append(";"));
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    public void deleteSubtaskID(Integer id) {
        subtasksID.remove(id);
    }

    public void addSubtaskID(Integer id) {
        if (!subtasksID.contains(id)) {
            subtasksID.add(id);
        }
    }

    public void setStartTimeOfEpic(LocalDateTime time) {
        super.setStartTime(time);
    }

    public void setEndOfEpic(LocalDateTime time) {
        endOfEpic = time;
    }

    @Override
    public Integer getDurationOfTask() {
        if (getStartTime() != null && endOfEpic != null) {
            return (int) Duration.between(getStartTime(), endOfEpic).toMinutes();
        }
        return null;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endOfEpic;
    }
}
