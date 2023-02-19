package ru.yandex.practicum.taskTracker.models;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksID;

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
                '}';
    }

    public String getListOfIdOfSubtasks() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subtasksID.size(); i++) {
            sb.append(subtasksID.get(i));
            if (i != subtasksID.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public void deleteSubtaskID(Integer id) {
        subtasksID.remove(id);
    }

    public void addSubtaskID(Integer id) {
        if (!subtasksID.contains(id)) {
            subtasksID.add(id);
        }
    }
}
