package ru.kanban.services;

import ru.kanban.models.Node;
import ru.kanban.models.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Long, Node<Task>> nodeMap = new HashMap<>();

    private final CustomLinkedList<Task> listHistory = new CustomLinkedList<>();

    @Override
    public void addToHistory(Task task) {
        remove(task.getId());
        Node<Task> newNode = listHistory.linkLast(task);
        nodeMap.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return listHistory.getTasks();
    }

    @Override
    public void remove(long id) {
        if (nodeMap.containsKey(id)) {
            listHistory.removeNode(nodeMap.get(id));
            nodeMap.remove(id);
        }
    }

    @Override
    public void clear() {
        nodeMap.clear();
    }
}

class CustomLinkedList<T extends Task> {

    private Node<T> head;

    private Node<T> tail;

    public Node<T> linkLast(T task) {
        Node<T> newNode = new Node<>(task);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        return newNode;
    }

    public List<T> getTasks() {
        List<T> tasks = new ArrayList<>();
        Node<T> current = tail;
        while (current != null) {
            tasks.add(current.getTask());
            current = current.getPrev();
        }
        return tasks;
    }

    public void removeNode(Node<T> node) {
        if (node == null) return;
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }
        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }
    }
}

