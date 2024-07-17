package ru.kanban.services;

import ru.kanban.models.Node;
import ru.kanban.models.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Long, Node> nodeMap = new HashMap<>();

    private final CustomLinkedList listHistory = new CustomLinkedList();

    private class CustomLinkedList {

        private Node head;

        private Node tail;

        public void linkLast(Task task) {
            Node newNode = new Node(task);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.setNext(newNode);
                newNode.setPrev(tail);
                tail = newNode;
            }
            nodeMap.put(task.getId(), newNode);
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node current = tail;
            while (current != null) {
                tasks.add(current.getTask());
                current = current.getPrev();
            }
            return tasks;
        }

        public void removeNode(Node node) {
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
            nodeMap.remove(node.getTask().getId());
        }
    }

    @Override
    public void addToHistory(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            listHistory.removeNode(nodeMap.get(task.getId()));
        }
        listHistory.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return listHistory.getTasks();
    }

    @Override
    public void remove(long id) {
        if (nodeMap.containsKey(id)) {
            listHistory.removeNode(nodeMap.get(id));
        }
    }
}
