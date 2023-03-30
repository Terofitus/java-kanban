package server.httpHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import models.SimpleTask;
import services.FileBackedTasksManager;
import services.TypeOfTask;

import java.io.IOException;

public class TaskHandler extends AbstractHandler {

    public TaskHandler(FileBackedTasksManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestQuery = exchange.getRequestURI().getRawQuery();
        switch (requestMethod) {
            case "GET":
                if (requestQuery != null) {
                    if (!requestQuery.startsWith("id=")) {
                        writeResponse(400, "Неверно переданные параметры запроса", exchange);
                        return;
                    }
                    try {
                        int idOfTask = Integer.parseInt(requestQuery.substring(3));
                        if (taskManager.getTaskByIdWithoutSaveInHistory(idOfTask) != null) {
                            String json = gson.toJson(taskManager.getTaskByIdWithoutSaveInHistory(idOfTask));
                            writeResponse(200, json, exchange);
                        } else {
                            writeResponse(400, "Нет задачи с таким id", exchange);
                        }
                    } catch (NumberFormatException exception) {
                        writeResponse(400, "Передан некорректный id задачи", exchange);
                    }
                } else {
                    String json = gson.toJson(taskManager.getListOfTasks(TypeOfTask.TASK));
                    writeResponse(200, json, exchange);
                }
                break;
            case "POST":
                SimpleTask task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), SimpleTask.class);
                boolean isAdded = taskManager.createNewTask(task);
                if (isAdded) {
                    writeResponse(201, "Задача успешно добавлена", exchange);
                } else {
                    writeResponse(500, "Не удалось добавить задачу", exchange);
                }
                break;
            case "DELETE":
                if (requestQuery != null) {
                    try {
                        int idOfTask = Integer.parseInt(requestQuery.substring(3));
                        if (taskManager.getTaskByIdWithoutSaveInHistory(idOfTask) != null) {
                            taskManager.deleteTaskById(idOfTask);
                            writeResponse(200, "Задача с id=" + idOfTask + "удалена", exchange);
                        } else {
                            writeResponse(400, "Нет задачи с таким id", exchange);
                        }
                    } catch (NumberFormatException exception) {
                        writeResponse(400, "Передан некорректный id задачи", exchange);
                    }
                } else {
                    taskManager.deleteAllTasks();
                    writeResponse(200, "Все задачи были удалены", exchange);
                }
                break;
            default:
                writeResponse(405, "Переданный метод не поддреживается", exchange);
        }
    }
}
