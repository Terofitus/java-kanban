package ru.yandex.practicum.taskTracker.service;

import ru.yandex.practicum.taskTracker.model.*;

import java.util.LinkedList;
import java.util.List;

class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history;

    InMemoryHistoryManager(){
        history = new FixedLinkedList<>();
    }

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    static class FixedLinkedList<E> extends LinkedList {
        int limit = 10;

        @Override
        public boolean add(Object o) {
            if (size() >= limit) {
                removeFirst();
            }
            return super.add(o);
        }
    }
}
