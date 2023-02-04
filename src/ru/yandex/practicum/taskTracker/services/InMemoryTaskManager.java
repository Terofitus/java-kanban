package ru.yandex.practicum.taskTracker.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.yandex.practicum.taskTracker.models.*;

class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasksById;
    HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasksById = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getListOfTasks(TypeOfTask type) {
        ArrayList<Task> tasksByType = new ArrayList<>();
        switch (type) {
            case TASK:
                processTask(tasksByType);
                for (Task task : tasksByType) {
                    historyManager.add(task);
                }
                break;
            case SIMPLE_TASK:
                processSimpleTask(tasksByType);
                for (Task task : tasksByType) {
                    historyManager.add(task);
                }
                break;
            case SUBTASK:
                processSubTask(tasksByType);
                for (Task task : tasksByType) {
                    historyManager.add(task);
                }
                break;
            case EPIC:
                processEpic(tasksByType);
                for (Task task : tasksByType) {
                    historyManager.add(task);
                }
                break;
        }
        if (tasksByType.isEmpty()) {
            System.out.println("Нет задач типа " + type + ".");
            return null;
        }
        return tasksByType;
    }

    private void processEpic(ArrayList<Task> tasksByType) {
        for (Task task: tasksById.values()) {
            if (task.getClass() == Epic.class) {
                tasksByType.add(task);
            }
        }
    }

    private void processSubTask(ArrayList<Task> tasksByType) {
        for (Task task: tasksById.values()) {
            if (task.getClass() == Subtask.class) {
                tasksByType.add(task);
            }
        }
    }

    private void processSimpleTask(ArrayList<Task> tasksByType) {
        for (Task task: tasksById.values()) {
            if (task.getClass() == SimpleTask.class) {
                tasksByType.add(task);
            }
        }
    }

    private void processTask(ArrayList<Task> tasksByType) {
        tasksByType.addAll(tasksById.values());
    }

    @Override
    public void createNewTask(SimpleTask task) {
        if (!tasksById.containsValue(task)) {
            tasksById.put(task.getId(), task);
        } else {
            System.out.println("Данная задача уже добавлена в трекер.");
        }
    }

    @Override
    public void createNewTask(Epic epic) {
        if (!tasksById.containsValue(epic)) {
            tasksById.put(epic.getId(), epic);
            for (Task task: tasksById.values()) {
                if (task.getClass() == Subtask.class && ((Subtask) task).getEpicID() == epic.getId()) {
                        epic.addSubtaskID(task.getId());
                }
            }
        } else {
            System.out.println("Данная задача уже добавлена в трекер.");
        }
    }

    @Override
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

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasksById.keySet()) {
            historyManager.remove(id);
        }
        tasksById.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        historyManager.add(tasksById.get(id));
        return tasksById.get(id);
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasksById.get(id);
        if (task.getClass() == Epic.class) {
            //без дополнительного списка и цикла на удаление выбрасывает ConcurrentModificationException
            List<Integer> idOfSubtasks = new ArrayList<>();
            for (Integer subtaskID : ((Epic) task).getSubtasksID()) {
                historyManager.remove(subtaskID);
                idOfSubtasks.add(subtaskID);
            }
            for (Integer subtaskID : idOfSubtasks) {
                tasksById.remove(subtaskID);
            }
        } else if (task.getClass() == Subtask.class) {
            ((Epic) tasksById.get(((Subtask) task).getEpicID())).deleteSubtaskID(task.getId());
        }
        tasksById.remove(task.getId());
        historyManager.remove(task.getId());
    }

    @Override
    public void printTasksByType(TypeOfTask type) {
        if (getListOfTasks(type) != null) {
            System.out.println(getListOfTasks(type));
        }
    }

    @Override
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

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        ((Epic) tasksById.get(subtask.getEpicID())).addSubtaskID(subtask.getId());
        updateStatusOfEpic(((Epic) tasksById.get(subtask.getEpicID())));
    }

}
