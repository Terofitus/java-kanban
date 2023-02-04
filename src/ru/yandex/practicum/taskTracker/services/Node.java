package ru.yandex.practicum.taskTracker.services;

import ru.yandex.practicum.taskTracker.models.Task;

public class Node<T extends Task> {
    Node<T> prev;
    T element;
    Node<T> next;

    public Node(ru.yandex.practicum.taskTracker.services.Node<T> prev, T element, ru.yandex.practicum.taskTracker.services.Node<T> next) {
        this.prev = prev;
        this.element = element;
        this.next = next;
    }
}

