package ru.kanban.utils;

import ru.kanban.services.HistoryManager;
import ru.kanban.services.InMemoryHistoryManager;
import ru.kanban.services.InMemoryTaskManager;
import ru.kanban.services.TaskManager;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
