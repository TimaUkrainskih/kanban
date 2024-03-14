package ru.kanban.services;

import ru.kanban.models.Epic;
import ru.kanban.models.Status;
import ru.kanban.models.Subtask;
import ru.kanban.models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class Manager {
    private static Map<Long, Task> taskList = new HashMap<>();
    private static Map<Long, Subtask> subtaskList = new HashMap<>();
    private static Map<Long, Epic> epicList = new HashMap<>();
    private static Long ID = 1L;

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

    public Optional<Task> createTask(Task task) {
        task.setId(ID);
        taskList.put(ID++, task);
        return Optional.of(task);
    }

    public Optional<Subtask> createSubtask(Subtask subtask) {
        if (epicList.containsKey(subtask.getEpicId())) {
            subtask.setId(ID);
            epicList.get(subtask.getEpicId()).addSubtaskId(ID);
            subtaskList.put(ID++, subtask);
            updateStatus(epicList.get(subtask.getEpicId()));
            return Optional.of(subtask);
        }
        return Optional.empty();
    }

    public Optional<Epic> createEpic(Epic epic) {
        epic.setId(ID);
        updateStatus(epic);
        epicList.put(ID++, epic);
        return Optional.of(epic);
    }

    public List<Task> listOfAllTasks() {
        List<Task> result = new ArrayList<>();
        result.addAll(taskList.values().stream().toList());
        result.addAll(subtaskList.values().stream().toList());
        result.addAll(epicList.values().stream().toList());
        return result;
    }

    public Optional<Task> findById(Long id) {
        if (taskList.containsKey(id)) {
            return Optional.ofNullable(taskList.get(id));
        } else if (subtaskList.containsKey(id)) {
            return Optional.ofNullable(subtaskList.get(id));
        } else if (epicList.containsKey(id)) {
            return Optional.ofNullable(epicList.get(id));
        }
        return Optional.empty();
    }

    public void deleteAllTasks() {
        taskList.clear();
        subtaskList.clear();
        epicList.clear();
    }

    public Optional<Task> updateTask(Task updatedTask) {
        if (taskList.containsKey(updatedTask.getId())) {
            taskList.put(updatedTask.getId(), updatedTask);
            return Optional.of(updatedTask);
        }
        return Optional.empty();
    }

    public Optional<Subtask> updateSubtask(Subtask updateSubtask) {
        if (subtaskList.containsKey(updateSubtask.getId())) {
            subtaskList.put(updateSubtask.getId(), updateSubtask);
            updateStatus(epicList.get(updateSubtask.getEpicId()));
            return Optional.of(updateSubtask);
        }
        return Optional.empty();
    }

    public Optional<Epic> updateEpic(Epic updatedEpic) {
        if (epicList.containsKey(updatedEpic.getId())) {
            updateStatus(updatedEpic);
            epicList.put(updatedEpic.getId(), updatedEpic);
            return Optional.of(updatedEpic);
        }
        return Optional.empty();
    }

    public boolean deleteTask(Long id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            return true;
        } else if (subtaskList.containsKey(id)) {
            Subtask deletedSubtask = subtaskList.remove(id);
            epicList.get(deletedSubtask.getEpicId()).deleteSubtaskId(deletedSubtask.getId());
            updateStatus(epicList.get(deletedSubtask.getEpicId()));
            return true;
        } else if (epicList.containsKey(id)) {
            epicList.remove(id);
            return true;
        }
        return false;
    }

    public List<Subtask> getSubtasksForEpic(Epic epic) {
        return epic.getListSubtaskId().stream()
                .filter(subtaskList::containsKey)
                .map(subtaskList::get)
                .collect(Collectors.toList());
    }

    public void increaseId() {
        ID++;
    }
}
