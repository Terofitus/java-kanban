package ru.yandex.practicum.taskTracker.model;

import ru.yandex.practicum.taskTracker.service.Status;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        updateStatus();
    }

    void updateStatus() {
        Status statusOfEpic = Status.NEW;
        boolean isInProgress = false;
        boolean isDone = false;
        if (subtasks != null) {
            for (Subtask subtask: subtasks) {
                if (subtask.getStatus() == Status.IN_PROGRESS) {
                    isInProgress = true;
                }
                isDone = subtask.getStatus() == Status.DONE;
            }
        }
        if (isDone) {
            statusOfEpic = Status.DONE;
        } else if (isInProgress) {
            statusOfEpic = Status.IN_PROGRESS;
        }
            setStatus(statusOfEpic);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    void addSubtask(Subtask subtask) {
        if (subtasks == null) {
            subtasks = new ArrayList<>();
        }
        subtasks.add(subtask);
        updateStatus();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", subtasks= [" + getListOfNamesOfSubtasks() +
                "], status=" + getStatus() +
                '}';
    }

    private String getListOfNamesOfSubtasks() {
        String namesOfSubtasks = "";
        if (subtasks != null) {
            for(Subtask subtask: subtasks) {
                namesOfSubtasks += subtask.getName() + "|";
            }
        }
        return namesOfSubtasks;
    }
}
