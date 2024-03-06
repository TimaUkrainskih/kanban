package ru.kanban.services;

import ru.kanban.models.Epic;
import ru.kanban.models.Subtask;
import ru.kanban.models.Task;

import java.util.*;

public class Manager {
    private static Map<Long, Task> taskList = new HashMap<>();
    private static Map<Long, Subtask> subtaskList = new HashMap<>();
    private static Map<Long, Epic> epicList = new HashMap<>();
    private static Long ID = 1L;

    private void updateStatusEpic() {
        for (Map.Entry<Long, Epic> map : epicList.entrySet()) {
            map.getValue().updateStatus();
        }
    }

    public Optional<Task> createTask(Task task) {
        task.setId(ID);
        taskList.put(ID++, task);
        return Optional.of(task);
    }

    public Optional<Subtask> createSubtask(Subtask subtask) {
        subtask.setId(ID);
        subtaskList.put(ID++, subtask);
        updateStatusEpic();
        return Optional.of(subtask);
    }

    public Optional<Epic> createEpic(Epic epic) {
        epic.setId(ID);
        epic.updateStatus();
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
            epicList.get(id).updateStatus();
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
        taskList.put(updatedTask.getId(), updatedTask);
        return Optional.of(updatedTask);
    }

    public Optional<Subtask> updateSubtask(Subtask updateSubtask) {
        subtaskList.put(updateSubtask.getId(), updateSubtask);
        updateStatusEpic();
        return Optional.of(updateSubtask);
    }

    public Optional<Epic> updateEpic(Epic updatedEpic) {
        updatedEpic.updateStatus();
        epicList.put(updatedEpic.getId(), updatedEpic);
        return Optional.of(updatedEpic);
    }

    public boolean deleteTask(Long id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            return true;
        } else if (subtaskList.containsKey(id)) {
            subtaskList.remove(id);
            updateStatusEpic();
            return true;
        } else if (epicList.containsKey(id)) {
            epicList.remove(id);
            return true;
        }
        return false;
    }

    public List<Subtask> getSubtasksForEpic(Epic epic) {
        return new ArrayList<>(epic.getSubtaskList());
    }

    public void increaseId() {
        ID++;
    }
}
