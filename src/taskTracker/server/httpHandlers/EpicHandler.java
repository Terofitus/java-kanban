package server.httpHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import models.Epic;
import models.Task;
import services.FileBackedTasksManager;
import services.TypeOfTask;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends AbstractHandler {

    public EpicHandler(FileBackedTasksManager taskManager, Gson gson) {
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
                    if (!requestQuery.startsWith("subtasksIdOfEpic=")) {
                        writeResponse(400, "Неверно переданные параметры запроса", exchange);
                        return;
                    }
                    try {
                        int idOfTask = Integer.parseInt(requestQuery.substring(17));
                        Task task = taskManager.getTaskByIdWithoutSaveInHistory(idOfTask);
                        if (task != null) {
                            if (task instanceof Epic) {
                                List<Integer> listOfId = ((Epic) task).getSubtasksID();
                                String json = gson.toJson(listOfId);
                                writeResponse(200, json, exchange);
                            } else {
                                writeResponse(400, "Не может вернуть список id подзадач," +
                                        "так как задача под переданным Id не является Epic", exchange);
                            }
                        } else {
                            writeResponse(400, "Нет задачи с таким id", exchange);
                        }
                    } catch (NumberFormatException exception) {
                        writeResponse(400, "Передан некорректный id задачи", exchange);
                    }
                } else {
                    String json = gson.toJson(taskManager.getListOfTasks(TypeOfTask.EPIC));
                    writeResponse(200, json, exchange);
                }
                break;
            case "POST":
                Epic task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Epic.class);
                boolean isAdded = taskManager.createNewTask(task);
                if (isAdded) {
                    writeResponse(201, "Задача успешно добавлена", exchange);
                } else {
                    writeResponse(500, "Не удалось добавить задачу", exchange);
                }
                break;
            case "DELETE":
                writeResponse(400, "Удаление задач происходит по пути /tasks/task", exchange);
            default:
                writeResponse(405, "Переданный метод не поддреживается", exchange);
        }
    }
}
