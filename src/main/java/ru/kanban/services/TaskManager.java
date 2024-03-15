package ru.kanban.services;

import ru.kanban.models.Epic;
import ru.kanban.models.Subtask;
import ru.kanban.models.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    List<Task> getHistory();

    Optional<Task> getTask(Long id);

    Optional<Task> getSubtask(Long id);

    Optional<Task> getEpic(Long id);

    Optional<Task> createTask(Task task);

    Optional<Subtask> createSubtask(Subtask subtask);

    Optional<Epic> createEpic(Epic epic);

    List<Task> listOfAllTasks();

    Optional<Task> findById(Long id);

    void deleteAllTasks();

    Optional<Task> updateTask(Task updatedTask);

    Optional<Subtask> updateSubtask(Subtask updateSubtask);

    Optional<Epic> updateEpic(Epic updatedEpic);

    boolean deleteTask(Long id);

    List<Subtask> getSubtasksForEpic(Epic epic);

}
