package ru.kanban.services;

import ru.kanban.models.Task;

import java.util.List;

public interface HistoryManager {
    void addToHistory(Task task);

    List<Task> getHistory();
}
