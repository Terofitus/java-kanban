package server.httpHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import services.FileBackedTasksManager;
import services.Managers;

public class HistoryHandler extends AbstractHandler {

    public HistoryHandler(FileBackedTasksManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        String requestQuery = exchange.getRequestURI().getRawQuery();
        if ("GET".equals(requestMethod)) {
            if (requestQuery != null) {
                writeResponse(400, "Возможно получение только полной истории", exchange);
            } else {
                String json = gson.toJson(Managers.getDefaultHistory().getHistory());
                writeResponse(200, json, exchange);
            }
        } else {
            writeResponse(405, "Переданный метод не поддреживается", exchange);
        }
    }
}
