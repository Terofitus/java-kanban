package ru.yandex.practicum.taskTracker.model;

import java.util.ArrayList;

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
                ", subtasks= [" + getListOfIdOfSubtasks() +
                "], status=" + getStatus() +
                '}';
    }

    private String getListOfIdOfSubtasks() {
        String namesOfSubtasks = "";
        if (subtasksID != null) {
            for(Integer subtaskID: subtasksID) {
                namesOfSubtasks += subtaskID + "|";
            }
        }
        return namesOfSubtasks;
    }

    public void deleteSubtaskID(Integer id) {
        subtasksID.remove(id);
    }

    public void addSubtaskID(Integer id) {
        subtasksID.add(id);
    }
}
