package ru.kanban.services;

import ru.kanban.models.Epic;
import ru.kanban.models.Subtask;
import ru.kanban.models.NormalTask;
import ru.kanban.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private static Map<Long, NormalTask> normalTaskList = new HashMap<>();
    private static Map<Long, Subtask> subtaskList = new HashMap<>();
    private static Map<Long, Epic> epicList = new HashMap<>();
    private static Long ID = 0L;

    public void create(Task task) {
        if (task instanceof NormalTask) {
            task.setId(++ID);
            normalTaskList.put(ID, (NormalTask) task);
        } else if (task instanceof Subtask) {
            task.setId(++ID);
            subtaskList.put(ID, (Subtask) task);
        } else {
            task.setId(++ID);
            epicList.put(ID, (Epic) task);
        }
    }

    public List<Task> listOfAllTasks() {
        List<Task> result = new ArrayList<>();
        result.addAll(normalTaskList.values().stream().toList());
        result.addAll(subtaskList.values().stream().toList());
        result.addAll(epicList.values().stream().toList());
        return result;
    }

    public Task findById(Long id) {
        if (normalTaskList.containsKey(id)) {
            return normalTaskList.get(id);
        } else if (subtaskList.containsKey(id)) {
            subtaskList.get(id);
        } else if (epicList.containsKey(id)) {
            epicList.get(id);
        }
        return null;
    }

    public void deleteAllTasks() {
        normalTaskList.clear();
        subtaskList.clear();
        epicList.clear();
    }

    public void updateTask(Task updatedTask) {
        if (updatedTask instanceof Epic) {
            epicList.put(updatedTask.getId(), (Epic) updatedTask);
        } else if (updatedTask instanceof Subtask) {
            subtaskList.put(updatedTask.getId(), (Subtask) updatedTask);
        } else {
            normalTaskList.put(updatedTask.getId(), (NormalTask) updatedTask);
        }
    }

    public boolean deleteTask(Long id) {
        if (normalTaskList.containsKey(id)) {
            normalTaskList.remove(id);
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
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Task task : epic.getSubtaskList()) {
            if (task instanceof Subtask) {
                epicSubtasks.add((Subtask) task);
            }
        }
        return epicSubtasks;
    }
}
