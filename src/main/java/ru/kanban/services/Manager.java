package ru.kanban.services;

import ru.kanban.models.Epic;
import ru.kanban.models.Subtask;
import ru.kanban.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private static Map<Long, Task> taskList = new HashMap<>();
    private static Map<Long, Subtask> subtaskList = new HashMap<>();
    private static Map<Long, Epic> epicList = new HashMap<>();
    private static Long ID = 1L;

    public void createTask(Task task) {
        if (task.getClass() != Task.class) {
            task.setId(ID);
            taskList.put(ID++, task);
        }
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

    public Task findById(Long id) {
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        } else if (subtaskList.containsKey(id)) {
            subtaskList.get(id);
        } else if (epicList.containsKey(id)) {
            epicList.get(id);
        }
        return null;
    }

    public void deleteAllTasks() {
        taskList.clear();
        subtaskList.clear();
        epicList.clear();
    }

    public void updateTask(Task updatedTask) {
        if (updatedTask.getClass() != Task.class) {
            taskList.put(updatedTask.getId(), updatedTask);
        }
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
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Task task : epic.getSubtaskList()) {
            if (task instanceof Subtask) {
                epicSubtasks.add((Subtask) task);
            }
        }
        return epicSubtasks;
    }

    public void increaseId() {
        ID++;
    }
}
