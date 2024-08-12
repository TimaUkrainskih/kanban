package ru.kanban.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kanban.models.Status;
import ru.kanban.models.Task;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addToHistory() {
        Task task1 = new Task("title1", "description1", 1, Status.NEW);
        Task task2 = new Task("title2", "description2", 2, Status.IN_PROGRESS);
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        List<Task> history = historyManager.getHistory();
        assertThat(2).isEqualTo(history.size());
        assertThat(task2).isEqualTo(history.get(0));
        assertThat(task1).isEqualTo(history.get(1));
    }

    @Test
    void removeTask() {
        Task task1 = new Task("title1", "description1", 1, Status.NEW);
        Task task2 = new Task("title2", "description2", 2, Status.IN_PROGRESS);
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();
        assertThat(1).isEqualTo(history.size());
        assertThat(task2).isEqualTo(history.get(0));
    }

    @Test
    void addCopyTask() {
        Task task = new Task("title1", "description1", 1, Status.NEW);
        historyManager.addToHistory(task);
        historyManager.addToHistory(task);
        List<Task> history = historyManager.getHistory();
        assertThat(1).isEqualTo(history.size());
        assertThat(task).isEqualTo(history.get(0));
    }

    @Test
    void addUpdateTask() {
        Task task = new Task("title", "description", 1, Status.NEW);
        Task taskUpdate = new Task("titleUpdate", "descriptionUpdate", 1, Status.IN_PROGRESS);
        historyManager.addToHistory(task);
        historyManager.addToHistory(taskUpdate);
        List<Task> history = historyManager.getHistory();
        assertThat(1).isEqualTo(history.size());
        assertThat(taskUpdate).isEqualTo(history.get(0));
    }

    @Test
    public void getHistoryEmpty() {
        List<Task> history = historyManager.getHistory();
        assertThat(0).isEqualTo(history.size());
    }
}