package ru.kanban.services;

import ru.kanban.exception.ManagerSaveException;
import ru.kanban.models.*;

import java.io.*;
import java.util.Optional;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(HistoryManager history, File file) {
        super(history);
        this.file = file;
    }

    public static void main(String[] args) {
        File file1 = new File("test.txt");
        InMemoryTaskManager manager = new FileBackedTasksManager(new InMemoryHistoryManager(), file1);
        Task task = new Task("title1", "description", Status.NEW);
        Subtask subtask = new Subtask("title1", "description", Status.NEW, 2L);
        Epic epic = new Epic("title1", "description", Status.NEW);
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file1, new InMemoryHistoryManager());
        for (Task task1 : fileBackedTasksManager.listOfAllTasks()) {
            System.out.println(task1.toString());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file, HistoryManager history) {
        FileBackedTasksManager manager = new FileBackedTasksManager(history, file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Task task = manager.fromString(line);
                if (task != null) {
                    manager.createTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла.", e);
        }
        return manager;
    }

    @Override
    public Optional<Task> createTask(Task task) {
        Optional<Task> result = super.createTask(task);
        save();
        return result;
    }

    @Override
    public Optional<Subtask> createSubtask(Subtask subtask) {
        Optional<Subtask> result = super.createSubtask(subtask);
        save();
        return result;
    }

    @Override
    public Optional<Epic> createEpic(Epic epic) {
        Optional<Epic> result = super.createEpic(epic);
        save();
        return result;
    }

    @Override
    public boolean deleteTask(Long id) {
        boolean result = super.deleteTask(id);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public Optional<Epic> updateEpic(Epic updatedEpic) {
        Optional<Epic> result = super.updateEpic(updatedEpic);
        save();
        return result;
    }

    @Override
    public Optional<Subtask> updateSubtask(Subtask updateSubtask) {
        Optional<Subtask> result = super.updateSubtask(updateSubtask);
        save();
        return result;
    }

    @Override
    public Optional<Task> updateTask(Task updatedTask) {
        Optional<Task> result = super.updateTask(updatedTask);
        save();
        return result;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : super.listOfAllTasks()) {
                writer.write(task.toString());
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл.", e);
        }
    }

    private Task fromString(String value) {
        Task task = null;
        String[] result = value.split(",");
        Type type = Type.fromValue(result[1]);
        switch (type) {
            case TASK:
                task = new Task(
                        result[2],
                        result[4],
                        Long.parseLong(result[0]),
                        Status.fromValue(result[3])
                );
                break;
            case EPIC:
                task = new Epic(
                        result[2],
                        result[4],
                        Long.parseLong(result[0]),
                        Status.fromValue(result[3])
                );
                break;
            case SUBTASK:
                task = new Subtask(
                        result[2],
                        result[4],
                        Long.parseLong(result[0]),
                        Status.fromValue(result[3]),
                        Long.parseLong(result[5])
                );
                break;
        }
        return task;
    }
}
