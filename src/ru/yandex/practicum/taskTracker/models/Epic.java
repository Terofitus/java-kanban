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
                ", subtasks= [" + getListOfIdOfSubtasks() +
                "], status=" + getStatus() +
                '}';
    }

    //оставил этот метод, так как в ревью присутствует правка на него. Переделал со StringBuilder,
    //а после обнаружил, что мог просто в toString() вставить сам ArrayList и получить тоже самое
    private String getListOfIdOfSubtasks() {
        StringBuilder namesOfSubtasks = new StringBuilder();
        if (subtasksID != null) {
            for( int i = 0; i < subtasksID.size(); i++) {
                namesOfSubtasks.append(subtasksID.get(i));
                if ( i != subtasksID.size() - 1) {
                    namesOfSubtasks.append(", ");
                }
            }
        }
        return String.valueOf(namesOfSubtasks);
    }

    public void deleteSubtaskID(Integer id) {
        subtasksID.remove(id);
    }

    public void addSubtaskID(Integer id) {
        subtasksID.add(id);
    }
}
