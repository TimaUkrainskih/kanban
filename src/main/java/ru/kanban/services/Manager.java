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

    public void createTask(Task task) {
        task.setId(ID);
        taskList.put(ID++, task);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(ID);
        subtaskList.put(ID++, subtask);
    }

    public void createEpic(Epic epic) {
        epic.setId(ID);
        epicList.put(ID++, epic);
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

    public void updateTask(Task updatedTask) {
        taskList.put(updatedTask.getId(), updatedTask);
    }

    public void updateSubtask(Subtask updateSubtask) {
        subtaskList.put(updateSubtask.getId(), updateSubtask);
    }

    public void updateEpic(Epic updatedEpic) {
        epicList.put(updatedEpic.getId(), updatedEpic);
    }

    public boolean deleteTask(Long id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            return true;
        } else if (subtaskList.containsKey(id)) {
            subtaskList.remove(id);
            return true;
        } else if (epicList.containsKey(id)) {
            subtaskList.remove(id);
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
