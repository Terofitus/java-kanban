package server.httpHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import models.Subtask;
import models.Task;
import services.FileBackedTasksManager;
import services.TypeOfTask;

import java.io.IOException;

public class SubtaskHandler extends AbstractHandler {

    public SubtaskHandler(FileBackedTasksManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestQuery = exchange.getRequestURI().getRawQuery();
        switch (requestMethod) {
            case "GET":
                processingMethodGet(requestQuery, exchange);
                break;
            case "POST":
                processingMethodPost(exchange);
                break;
            case "DELETE":
                writeResponse(400, "Удаление задач происходит по пути /tasks/task", exchange);
                break;
            default:
                writeResponse(405, "Переданный метод не поддреживается", exchange);
        }
    }

    private void processingMethodGet(String requestQuery, HttpExchange exchange) {
        if (requestQuery != null) {
            if (!requestQuery.startsWith("epicIdOfSubtask=")) {
                writeResponse(400, "Неверно переданные параметры запроса", exchange);
                return;
            }
            try {
                int idOfTask = Integer.parseInt(requestQuery.substring(16));
                Task task = taskManager.getTaskByIdWithoutSaveInHistory(idOfTask);
                if (task != null) {
                    if (task instanceof Subtask) {
                        Integer idOfEpic = ((Subtask) task).getEpicID();
                        String json = gson.toJson(idOfEpic);
                        writeResponse(200, json, exchange);
                    } else {
                        writeResponse(400, "Не может вернуть id Epic," +
                                "так как задача под переданным Id не является Subtask", exchange);
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
    }

    private void processingMethodPost(HttpExchange exchange) throws IOException {
        Subtask task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Subtask.class);
        boolean isAdded = taskManager.createNewTask(task);
        if (isAdded) {
            writeResponse(201, "Задача успешно добавлена", exchange);
        } else {
            writeResponse(500, "Не удалось добавить задачу", exchange);
        }
    }
}
