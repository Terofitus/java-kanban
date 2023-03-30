package server.httpHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import services.FileBackedTasksManager;

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
                String json = gson.toJson(taskManager.getPrioritizedTasks().pollFirst());
                writeResponse(200, json, exchange);
            }
        }
    }
}
