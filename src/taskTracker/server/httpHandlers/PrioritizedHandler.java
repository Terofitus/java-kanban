package server.httpHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import models.Task;
import services.FileBackedTasksManager;

import java.util.ArrayList;

public class PrioritizedHandler extends AbstractHandler {

    public PrioritizedHandler(FileBackedTasksManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        String requestQuery = exchange.getRequestURI().getRawQuery();
        if (!"GET".equals(requestMethod)) {
            writeResponse(405, "Поддерживается только метод GET", exchange);
        } else {
            if (requestQuery != null) {
                writeResponse(400, "Возможно получение только списка задач, " +
                        "сортированного по времени", exchange);
            } else {
                ArrayList<Task> list = new ArrayList<>();
                for (Task task : taskManager.getPrioritizedTasks()) {
                    list.add(task);
                }
                String json = gson.toJson(list);
                writeResponse(200, json, exchange);
            }
        }
    }
}
