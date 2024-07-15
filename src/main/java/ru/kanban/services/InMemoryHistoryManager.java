package ru.kanban.services;

import ru.kanban.models.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {

        Task task;

        Node prev;

        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private Node head;

    private Node tail;

    private final Map<Long, Node> nodeMap = new HashMap<>();

    @Override
    public void addToHistory(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeNode(nodeMap.get(task.getId()));
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(long id) {
        if (nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = tail;
        while (current != null) {
            tasks.add(current.task);
            current = current.prev;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node == null) return;
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        nodeMap.remove(node.task.getId());
    }
}
