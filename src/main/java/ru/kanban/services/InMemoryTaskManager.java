package ru.kanban.services;

import ru.kanban.models.Epic;
import ru.kanban.models.Status;
import ru.kanban.models.Subtask;
import ru.kanban.models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> taskList = new HashMap<>();
    private final Map<Long, Subtask> subtaskList = new HashMap<>();
    private final Map<Long, Epic> epicList = new HashMap<>();
    private Long id = 1L;
    private final HistoryManager history;

    public InMemoryTaskManager(HistoryManager history) {
        this.history = history;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public Optional<Task> createTask(Task task) {
        task.setId(id);
        taskList.put(id++, task);
        return Optional.of(task);
    }

    @Override
    public Optional<Subtask> createSubtask(Subtask subtask) {
        if (epicList.containsKey(subtask.getEpicId())) {
            subtask.setId(id);
            epicList.get(subtask.getEpicId()).addSubtaskId(id);
            subtaskList.put(id++, subtask);
            updateStatus(epicList.get(subtask.getEpicId()));
            return Optional.of(subtask);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Epic> createEpic(Epic epic) {
        epic.setId(id);
        updateStatus(epic);
        epicList.put(id++, epic);
        return Optional.of(epic);
    }

    @Override
    public List<Task> listOfAllTasks() {
        List<Task> result = new ArrayList<>();
        result.addAll(taskList.values().stream().toList());
        result.addAll(epicList.values().stream().toList());
        result.addAll(subtaskList.values().stream().toList());
        return result;
    }

    @Override
    public Optional<Task> findById(Long id) {
        if (taskList.containsKey(id)) {
            Task task = taskList.get(id);
            history.addToHistory(task);
            return Optional.ofNullable(task);
        } else if (subtaskList.containsKey(id)) {
            Subtask subtask = subtaskList.get(id);
            history.addToHistory(subtask);
            return Optional.ofNullable(subtask);
        } else if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);
            history.addToHistory(epic);
            return Optional.ofNullable(epic);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAllTasks() {
        taskList.clear();
        subtaskList.clear();
        epicList.clear();
        history.clear();
    }

    @Override
    public Optional<Task> updateTask(Task updatedTask) {
        if (taskList.containsKey(updatedTask.getId())) {
            taskList.put(updatedTask.getId(), updatedTask);
            return Optional.of(updatedTask);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Subtask> updateSubtask(Subtask updateSubtask) {
        if (subtaskList.containsKey(updateSubtask.getId())) {
            subtaskList.put(updateSubtask.getId(), updateSubtask);
            updateStatus(epicList.get(updateSubtask.getEpicId()));
            return Optional.of(updateSubtask);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Epic> updateEpic(Epic updatedEpic) {
        if (epicList.containsKey(updatedEpic.getId())) {
            updateStatus(updatedEpic);
            epicList.put(updatedEpic.getId(), updatedEpic);
            return Optional.of(updatedEpic);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteTask(Long id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            history.remove(id);
            return true;
        } else if (subtaskList.containsKey(id)) {
            Subtask deletedSubtask = subtaskList.remove(id);
            epicList.get(deletedSubtask.getEpicId()).deleteSubtaskId(deletedSubtask.getId());
            updateStatus(epicList.get(deletedSubtask.getEpicId()));
            history.remove(id);
            return true;
        } else if (epicList.containsKey(id)) {
            epicList.remove(id);
            history.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Subtask> getSubtasksForEpic(Epic epic) {
        return epic.getListSubtaskId().stream()
                .filter(subtaskList::containsKey)
                .map(subtaskList::get)
                .collect(Collectors.toList());
    }

    private void updateStatus(Epic epic) {
        boolean allNew = true;
        boolean allDone = true;
        for (Long taskId : epic.getListSubtaskId()) {
            Status subtaskStatus = subtaskList.get(taskId).getProgress();
            if (subtaskStatus != Status.NEW) {
                allNew = false;
            }
            if (subtaskStatus != Status.DONE) {
                allDone = false;
            }
        }
        if (epic.getListSubtaskId().isEmpty() || allNew) {
            epic.setProgress(Status.NEW);
        } else if (allDone) {
            epic.setProgress(Status.DONE);
        } else {
            epic.setProgress(Status.IN_PROGRESS);
        }
    }
}
