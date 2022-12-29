package ru.yandex.practicum.taskTracker.service;

import java.util.ArrayList;
import java.util.HashMap;
import ru.yandex.practicum.taskTracker.model.*;

public class TaskTrackerManager {
    private HashMap<Integer, Task> tasksById;
    private int id;

    public TaskTrackerManager() {
        tasksById = new HashMap<>();
        int id = 0;
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

    public void createNewTask(Task task) {
        if (!tasksById.containsValue(task)) {
            task.setId(id++);
            tasksById.put(task.getId(), task);
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
            for (Subtask subtask: ((Epic) task).getSubtasks()) {
                deleteTaskById(subtask.getId());
            }
        } else if (task.getClass() == Subtask.class) {
            ((Subtask) task).getEpic().getSubtasks().remove(task);
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
}
