package server.httpHandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.FileBackedTasksManager;

import java.io.IOException;

public abstract class AbstractHandler implements HttpHandler {
    FileBackedTasksManager taskManager;
    Gson gson;

    void writeResponse(int statusCode, String responseMessage, HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(statusCode, 0);
            exchange.getResponseBody().write(responseMessage.getBytes());
            exchange.getResponseBody().close();
        } catch (IOException e) {
            System.out.println("Произошли ошибка при попытке создать ответ с сервера.");
        }
    }
}
