package services;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class InMemoryHistoryManager implements HistoryManager {
    final LinkedListOfHistory<Task> history;


    InMemoryHistoryManager() {
        history = new LinkedListOfHistory<>();
    }

    @Override
    public void add(Task task) {
        history.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public boolean remove(int id) {
        return history.remove(id);
    }

    public void deleteHistory() {
        history.getTasks().stream().map(Task::getId).forEach(history::remove);
    }

    static final class LinkedListOfHistory<T extends Task> {
        private final HashMap<Integer, Node<T>> mapOfHistoryOfTasks;
        private Node<T> head;
        private Node<T> tail;

        public LinkedListOfHistory() {
            this.head = null;
            this.tail = null;
            mapOfHistoryOfTasks = new HashMap<>();
        }


        private void linkLast(T ob) {
            if (this.head == null) {
                Node<T> node = new Node<>(null, ob, null);
                this.head = this.tail = node;
                mapOfHistoryOfTasks.put(ob.getId(), node);
            } else {
                Node<T> oldTail = this.tail;
                if (mapOfHistoryOfTasks.containsKey(ob.getId())) {
                    if (tail.element.equals(ob)) return;
                    remove(ob.getId());
                }
                oldTail.next = tail = new Node<>(oldTail, ob, null);
                mapOfHistoryOfTasks.put(ob.getId(), this.tail);
            }
        }

        private ArrayList<Task> getTasks() {
            ArrayList<Task> arrayList = new ArrayList<>();
            if (this.tail != null) {
                Node<T> node = this.head;
                while (node != null) {
                    arrayList.add(node.element);
                    node = node.next;
                }
            }
            return arrayList;
        }


        private boolean remove(int id) {
            if (mapOfHistoryOfTasks.containsKey(id)) {
                Node<T> oldNode = mapOfHistoryOfTasks.get(id);
                if (oldNode == head && oldNode == tail && oldNode.prev == null) {
                    mapOfHistoryOfTasks.remove(id);
                    head = tail = null;
                } else if (oldNode.prev != null && oldNode.next != null) {
                    oldNode.prev.next = oldNode.next;
                    oldNode.next.prev = oldNode.prev;
                    mapOfHistoryOfTasks.remove(id);
                } else if (oldNode.prev != null) {
                    tail = oldNode.prev;
                    oldNode.prev.next = null;
                    mapOfHistoryOfTasks.remove(id);
                } else if (oldNode.next != null) {
                    head = oldNode.next;
                    oldNode.next.prev = null;
                    mapOfHistoryOfTasks.remove(id);
                }
                return true;
            }
            return false;
        }

    }
}

