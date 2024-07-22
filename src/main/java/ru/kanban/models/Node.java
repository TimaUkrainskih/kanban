package ru.kanban.models;

public class Node<T extends Task> {

    private T task;

    private Node<T> prev;

    private Node<T> next;

    public Node(T task) {
        this.task = task;
    }

    public T getTask() {
        return task;
    }

    public void setTask(T task) {
        this.task = task;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
