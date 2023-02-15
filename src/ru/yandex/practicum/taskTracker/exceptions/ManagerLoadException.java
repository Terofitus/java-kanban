package ru.yandex.practicum.taskTracker.exceptions;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException(String message) {
        super(message);
    }
}
