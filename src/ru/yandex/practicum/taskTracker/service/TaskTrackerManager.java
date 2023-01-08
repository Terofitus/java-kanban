package ru.yandex.practicum.taskTracker.service;

import java.util.ArrayList;
import java.util.HashMap;
import ru.yandex.practicum.taskTracker.model.*;

public class TaskTrackerManager {
    private HashMap<Integer, Task> tasksById;

    public TaskTrackerManager() {
        tasksById = new HashMap<>();
    }

    public ArrayList getListOfTasks(TypeOfTask type) {
        ArrayList<Task> tasksByType = new ArrayList<>();
        switch (type) {
            case TASK:
                tasksByType.addAll(tasksById.values());
                break;
            case SIMPLE_TASK:
                for (Task task: tasksById.values()) {
                    if (task.getClass() == SimpleTask.class) {
                        tasksByType.add(task);
                    }
                }
                break;
            case SUBTASK:
                for (Task task: tasksById.values()) {
                    if (task.getClass() == Subtask.class) {
                        tasksByType.add(task);
                    }
                }
                break;
            case EPIC:
                for (Task task: tasksById.values()) {
                    if (task.getClass() == Epic.class) {
                        tasksByType.add(task);
                    }
                }
                break;
        }
        if (tasksByType.isEmpty()) {
            System.out.println("Нет задач типа " + type + ".");
            return null;
        }
        return tasksByType;
    }

    public void createNewTask(SimpleTask task) {
        if (!tasksById.containsValue(task)) {
            tasksById.put(task.getId(), task);
        } else {
            System.out.println("Данная задача уже добавлена в трекер.");
        }
    }

    public void createNewTask(Epic epic) {
        if (!tasksById.containsValue(epic)) {
            tasksById.put(epic.getId(), epic);
            for (Task task: tasksById.values()) {
                if (task.getClass() == Subtask.class) {
                    if (((Subtask) task).getEpicID() == epic.getId()){
                        epic.addSubtaskID(task.getId());
                    }
                }
            }
        } else {
            System.out.println("Данная задача уже добавлена в трекер.");
        }
    }

    public void createNewTask(Subtask task) {
        if (!tasksById.containsValue(task)) {
            tasksById.put(task.getId(), task);
            if (tasksById.containsValue(tasksById.get(task.getEpicID()))) {
                addSubtaskToEpic(task);
            } else {
                System.out.println("Вы не добавили глобальную задачу для данной подзадачи в таблицу!");
            }
        } else {
            System.out.println("Данная задача уже добавлена в трекер.");
        }
    }

    public void deleteAllTasks() {
        tasksById.clear();
    }

    public Task getTaskById(Integer id) {
        return tasksById.get(id);
    }

    public void deleteTaskById(int id) {
        Task task = tasksById.get(id);
        if (task.getClass() == Epic.class) {
            for (Integer subtaskID: ((Epic) task).getSubtasksID()) {
                deleteTaskById(subtaskID);
            }
        } else if (task.getClass() == Subtask.class) {
            ((Epic) tasksById.get(((Subtask) task).getEpicID())).deleteSubtaskID(task.getId());
        }
        tasksById.remove(task.getId());
    }

    public void printTasksAllTypeByType() {
        printTasksByType(TypeOfTask.TASK);
        printTasksByType(TypeOfTask.SIMPLE_TASK);
        printTasksByType(TypeOfTask.EPIC);
        printTasksByType(TypeOfTask.SUBTASK);
    }

    public void printTasksByType(TypeOfTask type) {
        if (getListOfTasks(type) != null) {
            System.out.println(getListOfTasks(type));
        }
    }

    public void updateStatusOfEpic(Epic epic) {
        boolean isInProgress = false;
        boolean isDone = false;
        if (!epic.getSubtasksID().isEmpty()) {
            for (Integer subtaskID: epic.getSubtasksID()) {
                if (tasksById.get(subtaskID).getStatus() == Status.IN_PROGRESS) {
                    isInProgress = true;
                }
                isDone = tasksById.get(subtaskID).getStatus() == Status.DONE;
            }
        }
        if (isDone) {
            epic.setStatus(Status.DONE);
        } else if (isInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private void addSubtaskToEpic(Subtask subtask) {
        ((Epic) tasksById.get(subtask.getEpicID())).addSubtaskID(subtask.getId());
        updateStatusOfEpic(((Epic) tasksById.get(subtask.getEpicID())));
    }
}
