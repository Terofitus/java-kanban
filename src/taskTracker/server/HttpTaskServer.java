package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import server.httpHandlers.*;
import services.FileBackedTasksManager;
import services.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final Gson gson;
    private final FileBackedTasksManager fileManager;

    public HttpTaskServer() throws IOException {
        fileManager = Managers.getFileBackedTasksManager();
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(fileManager, gson));
        httpServer.createContext("/tasks", new PrioritizedHandler(fileManager, gson));
        httpServer.createContext("/tasks/epic", new EpicHandler(fileManager, gson));
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(fileManager, gson));
        httpServer.createContext("/tasks/history", new HistoryHandler(fileManager, gson));
        httpServer.start();
    }

    public HttpTaskServer(boolean withLoad, boolean withSave) throws IOException {
        fileManager = new FileBackedTasksManager(withLoad, withSave);
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(fileManager, gson));
        httpServer.createContext("/tasks", new PrioritizedHandler(fileManager, gson));
        httpServer.createContext("/tasks/epic", new EpicHandler(fileManager, gson));
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(fileManager, gson));
        httpServer.createContext("/tasks/history", new HistoryHandler(fileManager, gson));
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
