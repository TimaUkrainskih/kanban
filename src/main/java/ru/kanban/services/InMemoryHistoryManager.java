package ru.kanban.services;

import ru.kanban.models.Task;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_SIZE = 10;
    private final Deque<Task> history = new LinkedList<>();

    @Override
    public void addToHistory(Task task) {
        if (history.size() >= HISTORY_SIZE) {
            history.pollLast();
        }
        history.offerFirst(task);
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }
}
