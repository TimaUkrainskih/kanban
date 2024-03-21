package ru.kanban.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kanban.models.Epic;
import ru.kanban.models.Status;
import ru.kanban.models.Subtask;
import ru.kanban.models.Task;
import ru.kanban.utils.Managers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager manager;
    private Task task;
    private Subtask subtask;
    private Epic epic;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
        task = new Task("title1", "description", Status.NEW);
        subtask = new Subtask("title1", "description", Status.NEW, 2L);
        epic = new Epic("title1", "description", Status.NEW);
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);

    }

    @Test
    void getHistory() {
        manager.findById(1L);
        manager.findById(2L);
        manager.findById(3L);
        epic.addSubtaskId(3L);
        List<Task> result = manager.getHistory();
        List<Task> expected = List.of(subtask, epic, task);
        assertThat(result.equals(expected));
    }

    @Test
    void createTask() {
        Task expected = new Task("titleCreateNewTask",
                "descriptionCreateNewTask",
                Status.IN_PROGRESS);
        manager.createTask(expected);
        Task result = manager.findById(4L).get();
        assertThat(expected.equals(result));
    }

    @Test
    void createSubtask_NoEpic() {
        Subtask subtaskNoEpic = new Subtask("titleCreateNewSubtask",
                "descriptionCreateNewSubtask",
                Status.IN_PROGRESS, 10L);
        manager.createSubtask(subtaskNoEpic);
        Optional<Task> result = manager.findById(4L);
        assertTrue(result.isEmpty());
    }

    @Test
    void createSubtask_ExistEpic() {
        Subtask subtaskNoEpic = new Subtask("titleCreateNewSubtask",
                "descriptionCreateNewSubtask",
                Status.IN_PROGRESS, 2L);
        manager.createSubtask(subtaskNoEpic);
        Task result = manager.findById(4L).get();
        assertThat(subtaskNoEpic.equals(result));
    }

    @Test
    void createEpic() {
        Epic expected = new Epic("titleCreateNewEpic",
                "descriptionCreateNewEpic",
                Status.NEW);
        manager.createEpic(expected);
        Task result = manager.findById(4L).get();
        assertThat(expected.equals(result));
    }

    @Test
    void listOfAllTasks() {
        List<Task> result = manager.listOfAllTasks();
        epic.addSubtaskId(3L);
        List<Task> expected = List.of(task, subtask, epic);
        assertTrue(result.containsAll(expected));
    }

    @Test
    void findById_ReturnsOptionalContainingTask() {
        Task task = manager.findById(3L).get();
        assertThat(task.equals(subtask));
    }

    @Test
    void findById_ReturnsEmptyOptional() {
        assertTrue(manager.findById(4L).isEmpty());
    }

    @Test
    void updateTask() {
        Task expected = new Task("titleUpdateTask",
                "descriptionUpdateTask",
                1L,
                Status.IN_PROGRESS);
        manager.updateTask(expected);
        Task result = manager.findById(1L).get();
        assertThat(expected.equals(result));
    }

    @Test
    void updateSubtask() {
        Subtask expected = new Subtask("titleUpdateSubtask",
                "descriptionUpdateSubtask",
                3L,
                Status.IN_PROGRESS,
                2L);
        manager.updateSubtask(expected);
        Task result = manager.findById(3L).get();
        assertThat(expected.equals(result));
    }

    @Test
    void updateEpic() {
        Epic expected = new Epic("titleUpdateEpic",
                "descriptionUpdateEpic",
                2L,
                Status.IN_PROGRESS);
        manager.updateEpic(expected);
        Task result = manager.findById(2L).get();
        assertThat(expected.equals(result));
    }

    @Test
    void updateStatusEpic() {
        Subtask sb = new Subtask("titleSubtask",
                "descriptionSubtask",
                Status.IN_PROGRESS,
                2L);
        manager.createSubtask(sb);
        Epic expected = new Epic("title1", "description", 2L, Status.IN_PROGRESS);
        expected.addSubtaskId(3L);
        expected.addSubtaskId(4L);
        Task result = manager.findById(2L).get();
        assertThat(expected.equals(result));
    }

    @Test
    void deleteTask() {
        manager.deleteTask(3L);
        manager.deleteTask(2L);
        List<Task> expected = List.of(task);
        List<Task> result = manager.listOfAllTasks();
        assertThat(expected.equals(result));
    }

    @Test
    void getSubtasksForEpic() {
        List<Subtask> result = manager.getSubtasksForEpic((Epic) manager.findById(2L).get());
        List<Subtask> expected = List.of(subtask);
        assertThat(expected.equals(result));
    }
}