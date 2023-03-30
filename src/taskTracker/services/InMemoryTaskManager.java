package services;

import models.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasksById;
    private final TreeSet<Task> sortedSimpleAndSubTaskByStartTime;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        tasksById = new HashMap<>();
        Task.resetCounterOfId();
        sortedSimpleAndSubTaskByStartTime = new TreeSet<>((o1, o2) -> {
            if ((o1.getStartTime() != null && o2.getStartTime() != null)
                    && o1.getStartTime().isBefore(o2.getStartTime())) return -1;
            if ((o1.getStartTime() != null && o2.getStartTime() != null)
                    && o1.getStartTime().isEqual(o2.getStartTime())) return 0;
            if (o1.equals(o2)) return 0;
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
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedSimpleAndSubTaskByStartTime;
    }

    @Override
    public boolean createNewTask(SimpleTask task) {
        if (!validationOfTaskExecutionTime(task)) {
            System.out.println("Данная задача не может быть создана, так как пересекается по времени выполнения " +
                    "с уже созданными задачами!");
            return false;
        }
        if (!tasksById.containsValue(task)) {
            Task.setNewId(task);
        } else {
            sortedSimpleAndSubTaskByStartTime.remove(task);
            for (Task oldTask : tasksById.values()) {
                if (oldTask.equals(task)) {
                    task.setOldId(oldTask.getId());
                }
            }
        }
        tasksById.put(task.getId(), task);
        sortedSimpleAndSubTaskByStartTime.add(task);
        return true;
    }

    @Override
    public boolean createNewTask(Epic epic) {
        if (!tasksById.containsValue(epic)) {
            Task.setNewId(epic);
        } else {
            for (Task task : tasksById.values()) {
                if (task.equals(epic)) {
                    epic.setOldId(task.getId());
                }
            }
        }
        tasksById.put(epic.getId(), epic);
        for (Task task : tasksById.values()) {
            if (task instanceof Subtask && ((Subtask) task).getEpicID() == epic.getId()) {
                epic.addSubtaskID(task.getId());
                updateEpic(epic);
            }
        }
        return true;
    }

    @Override
    public boolean createNewTask(Subtask task) {
        if (!validationOfTaskExecutionTime(task)) {
            System.out.println("Данная задача не может быть создана, так как пересекается по времени выполнения " +
                    "с уже созданными задачами!");
            return false;
        }
        if (!tasksById.containsValue(task)) {
            Task.setNewId(task);
        } else {
            sortedSimpleAndSubTaskByStartTime.remove(task);
            for (Task oldTask : tasksById.values()) {
                if (oldTask.equals(task)) {
                    task.setOldId(oldTask.getId());
                }
            }
        }
        tasksById.put(task.getId(), task);
        sortedSimpleAndSubTaskByStartTime.add(task);
        if (tasksById.containsValue(tasksById.get(task.getEpicID()))) {
            addSubtaskToEpic(task);
        } else {
            System.out.println("Вы не добавили глобальную задачу для данной подзадачи в таблицу!");
        }
        return true;
    }

    private boolean validationOfTaskExecutionTime(Task task) {
        boolean isFreeTime = true;
        for (Task taskFromSet : sortedSimpleAndSubTaskByStartTime) {
            if ((taskFromSet.getStartTime().isAfter(task.getStartTime())
                    && taskFromSet.getStartTime().isBefore(task.getEndTime()))
                    || (taskFromSet.getEndTime().isAfter(task.getStartTime())
                    && taskFromSet.getEndTime().isBefore((task.getEndTime())))
                    || taskFromSet.getStartTime().isEqual(task.getStartTime())
                    || taskFromSet.getEndTime().isEqual(task.getEndTime())) isFreeTime = false;
        }
        return isFreeTime;
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasksById.keySet()) {
            historyManager.remove(id);
        }
        tasksById.clear();
        sortedSimpleAndSubTaskByStartTime.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        if (tasksById.containsKey(id)) {
            historyManager.add(tasksById.get(id));
        }
        return tasksById.get(id);
    }

    public Task getTaskByIdWithoutSaveInHistory(Integer id) {
        return tasksById.get(id);
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasksById.containsKey(id)) return;
        Task task = tasksById.get(id);
        if (task instanceof Epic) {
            //без дополнительного списка и цикла на удаление выбрасывает ConcurrentModificationException
            List<Integer> idOfSubtasks = new ArrayList<>();
            for (Integer subtaskID : ((Epic) task).getSubtasksID()) {
                historyManager.remove(subtaskID);
                idOfSubtasks.add(subtaskID);
            }
            for (Integer subtaskID : idOfSubtasks) {
                sortedSimpleAndSubTaskByStartTime.remove(tasksById.get(subtaskID));
                tasksById.remove(subtaskID);
            }
        } else if (task instanceof Subtask) {
            ((Epic) tasksById.get(((Subtask) task).getEpicID())).deleteSubtaskID(task.getId());
        }
        tasksById.remove(task.getId());
        historyManager.remove(task.getId());
        sortedSimpleAndSubTaskByStartTime.remove(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        boolean isNew = true;
        boolean isInProgress = false;
        boolean isDone = false;
        if (!epic.getSubtasksID().isEmpty()) {
            isNew = false;
            for (Integer subtaskID : epic.getSubtasksID()) {
                Subtask subTask = (Subtask) tasksById.get(subtaskID);
                if (subTask != null) {
                    setStartAndEndTimeOfEpic(epic, subTask);
                    switch (subTask.getStatus()) {
                        case IN_PROGRESS:
                            isInProgress = true;
                            break;
                        case DONE:
                            isDone = true;
                            break;
                        default:
                            isNew = true;
                    }
                }
            }
        }
        if (isDone && !isInProgress && !isNew) {
            epic.setStatus(Status.DONE);
        } else if (isInProgress || isDone) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    private void setStartAndEndTimeOfEpic(Epic epic, Subtask subTask) {
        if (epic.getStartTime() == null || subTask.getStartTime().isBefore(epic.getStartTime())) {
            epic.setStartTimeOfEpic(subTask.getStartTime());
        }
        if (epic.getEndTime() == null || subTask.getEndTime().isAfter(epic.getEndTime())) {
            epic.setEndOfEpic(subTask.getEndTime());
        }
    }

    private void addSubtaskToEpic(Subtask subtask) {
        Epic epic = ((Epic) tasksById.get(subtask.getEpicID()));
        if (epic != null) {
            epic.addSubtaskID(subtask.getId());
            updateEpic(epic);
        } else {
            System.out.println("Данного эпика не существует!");
        }
    }
}
