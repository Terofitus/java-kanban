package ru.yandex.practicum.taskTracker.models;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasksID;

    public Epic(String name, String description) {
        super(name, description);
        super.setId();
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
                '}';
    }

    private String getListOfIdOfSubtasks() {
        return subtasksID.toString();
    }

    public void deleteSubtaskID(Integer id) {
        subtasksID.remove(id);
    }

    public void addSubtaskID(Integer id) {
        subtasksID.add(id);
    }
}
