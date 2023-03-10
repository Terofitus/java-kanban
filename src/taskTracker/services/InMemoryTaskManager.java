package services;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasksById;
    private final TreeSet<Task> sortedTaskByStartTime;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        tasksById = new HashMap<>();
        sortedTaskByStartTime = new TreeSet<>((o1, o2) -> {
            if ((o1.getStartTime() != null && o2.getStartTime() != null)
                    && o1.getStartTime().isBefore(o2.getStartTime())) return -1;
            if ((o1.getStartTime() != null && o2.getStartTime() != null)
                    && o1.getStartTime().isEqual(o2.getStartTime())) return 0;
            return 1;
        });
    }

    boolean isNotEmptyMap() {
        return !tasksById.isEmpty();
    }

    @Override
    public ArrayList<Task> getListOfTasks(TypeOfTask type) {
        ArrayList<Task> tasksByType = getListOfTasksWithoutSaveInHistory(type);
        for (Task task : tasksByType) {
            historyManager.add(task);
        }
        if (tasksByType.isEmpty()) {
            System.out.println("Нет задач типа " + type + ".");
        }
        return tasksByType;
    }

    ArrayList<Task> getListOfTasksWithoutSaveInHistory(TypeOfTask type) {
        ArrayList<Task> tasksByType = new ArrayList<>();
        switch (type) {
            case SIMPLE_TASK:
                processSimpleTask(tasksByType);
                break;
            case SUBTASK:
                processSubTask(tasksByType);
                break;
            case EPIC:
                processEpic(tasksByType);
                break;
            default:
                processTask(tasksByType);
        }
        return tasksByType;
    }

    private void processEpic(ArrayList<Task> tasksByType) {
        tasksById.values().stream().filter(task -> task.getClass() == Epic.class).forEach(tasksByType::add);
    }

    private void processSubTask(ArrayList<Task> tasksByType) {
        tasksById.values().stream().filter(task -> task.getClass() == Subtask.class).forEach(tasksByType::add);
    }

    private void processSimpleTask(ArrayList<Task> tasksByType) {
        tasksById.values().stream().filter(task -> task.getClass() == SimpleTask.class).forEach(tasksByType::add);
    }

    private void processTask(ArrayList<Task> tasksByType) {
        tasksByType.addAll(tasksById.values());
    }

    @Override
    public void createNewTask(SimpleTask task) {
        if (!tasksById.containsValue(task)) {
            Task.setNewId(task);
            tasksById.put(task.getId(), task);
            sortedTaskByStartTime.add(task);
        } else {
            for (Task oldTask : tasksById.values()) {
                if (oldTask.equals(task)) {
                    task.setOldId(oldTask.getId());
                }
            }
            tasksById.put(task.getId(), task);
            sortedTaskByStartTime.remove(task);
            sortedTaskByStartTime.add(task);
        }
    }

    @Override
    public void createNewTask(Epic epic) {
        if (!tasksById.containsValue(epic)) {
            Task.setNewId(epic);
            tasksById.put(epic.getId(), epic);
            sortedTaskByStartTime.add(epic);
        } else {
            for (Task task : tasksById.values()) {
                if (task.equals(epic)) {
                    epic.setOldId(task.getId());
                }
            }
            tasksById.put(epic.getId(), epic);
            sortedTaskByStartTime.remove(epic);
            sortedTaskByStartTime.add(epic);
        }
        for (Task task : tasksById.values()) {
            if (task.getClass() == Subtask.class && ((Subtask) task).getEpicID() == epic.getId()) {
                epic.addSubtaskID(task.getId());
                updateEpic(epic);
            }
        }
    }

    @Override
    public void createNewTask(Subtask task) {
        if (!tasksById.containsValue(task)) {
            Task.setNewId(task);
            tasksById.put(task.getId(), task);
            sortedTaskByStartTime.add(task);
        } else {
            for (Task oldTask : tasksById.values()) {
                if (oldTask.equals(task)) {
                    task.setOldId(oldTask.getId());
                }
            }
            tasksById.put(task.getId(), task);
            sortedTaskByStartTime.remove(task);
            sortedTaskByStartTime.add(task);
        }
        if (tasksById.containsValue(tasksById.get(task.getEpicID()))) {
            addSubtaskToEpic(task);
        } else {
            System.out.println("Вы не добавили глобальную задачу для данной подзадачи в таблицу!");
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasksById.keySet()) {
            historyManager.remove(id);
        }
        tasksById.clear();
        sortedTaskByStartTime.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        if (tasksById.containsKey(id)) {
            historyManager.add(tasksById.get(id));
        }
        return tasksById.get(id);
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasksById.containsKey(id)) return;
        Task task = tasksById.get(id);
        if (task.getClass() == Epic.class) {
            //без дополнительного списка и цикла на удаление выбрасывает ConcurrentModificationException
            List<Integer> idOfSubtasks = new ArrayList<>();
            for (Integer subtaskID : ((Epic) task).getSubtasksID()) {
                historyManager.remove(subtaskID);
                idOfSubtasks.add(subtaskID);
            }
            for (Integer subtaskID : idOfSubtasks) {
                sortedTaskByStartTime.remove(tasksById.get(subtaskID));
                tasksById.remove(subtaskID);
            }
        } else if (task.getClass() == Subtask.class) {
            ((Epic) tasksById.get(((Subtask) task).getEpicID())).deleteSubtaskID(task.getId());
        }
        tasksById.remove(task.getId());
        historyManager.remove(task.getId());
        sortedTaskByStartTime.remove(task);
    }

    @Override
    public void printTasksByType(TypeOfTask type) {
        if (getListOfTasks(type) != null) {
            System.out.println(getListOfTasks(type));
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        boolean isInProgress = false;
        boolean isDone = false;
        if (!epic.getSubtasksID().isEmpty()) {
            for (Integer subtaskID : epic.getSubtasksID()) {
                Task subTask = tasksById.get(subtaskID);
                if (epic.getStartTime() == null || subTask.getStartTime().isBefore(epic.getStartTime())) {
                    epic.setStartTimeOfEpic(subTask.getStartTime());
                }
                if (epic.getEndTime() == null || subTask.getEndTime().isAfter(epic.getEndTime())) {
                    epic.setEndOfEpic(subTask.getEndTime());
                }
                if (subTask.getStatus() == Status.IN_PROGRESS) {
                    isInProgress = true;
                }
                isDone = tasksById.get(subtaskID).getStatus() == Status.DONE;
            }
        }
        if (isDone) {
            epic.setStatus(Status.DONE);
        } else if (isInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
        sortedTaskByStartTime.remove(epic);
        sortedTaskByStartTime.add(epic);
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        Epic epic = ((Epic) tasksById.get(subtask.getEpicID()));
        if (epic != null) {
            epic.addSubtaskID(subtask.getId());
            updateEpic(epic);
        } else {
            System.out.println("Данного эпика не существует!");
        }
    }
}
