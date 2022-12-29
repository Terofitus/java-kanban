package ru.yandex.practicum.taskTracker.model;

import ru.yandex.practicum.taskTracker.service.Status;

public class Subtask extends Task {
    private final Epic epic;

    public Epic getEpic() {
        return epic;
    }

    public void setStatus(Status status) {
        super.setStatus(status);
        epic.updateStatus();
    }

    public Subtask(String name, String description, Epic epic, Status status) {
        super(name, description);
        super.setStatus(status);
        this.epic = epic;
        epic.addSubtask(this);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epic=" + epic.getName() +
                '}';
    }
}
