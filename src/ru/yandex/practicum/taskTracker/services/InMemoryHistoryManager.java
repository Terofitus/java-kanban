package ru.yandex.practicum.taskTracker.services;

import ru.yandex.practicum.taskTracker.models.*;

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

    static class FixedLinkedList<E> extends LinkedList<E> {
        int limit = 10;

        @Override
        public boolean add(E e) {
            if (size() >= limit) {
                removeFirst();
            }
            return super.add(e);
        }
    }
}
