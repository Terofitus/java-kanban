package services;

import models.Task;

public class Node<T extends Task> {
    Node<T> prev;
    T element;
    Node<T> next;

    public Node(Node<T> prev, T element, Node<T> next) {
        this.prev = prev;
        this.element = element;
        this.next = next;
    }
}

