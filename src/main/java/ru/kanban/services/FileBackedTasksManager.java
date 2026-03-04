package ru.kanban.services;

import ru.kanban.exception.InvalidDataFormatException;
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

    public static FileBackedTasksManager loadFromFile(File file, HistoryManager history) {
        FileBackedTasksManager manager = new FileBackedTasksManager(history, file);
        isLoad = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Task task = manager.fromString(line);
                switch (task.getType()) {
                    case TASK:
                        manager.createTask(task);
                        break;
                    case EPIC:
                        manager.createEpic((Epic) task);
                        break;
                    case SUBTASK:
                        manager.createSubtask((Subtask) task);
                        break;
                }
            }
        } catch (InvalidDataFormatException e) {
            throw new ManagerSaveException("Ошибка при обработке данных.", e);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла.", e);
        } finally {
            isLoad = false;
        }
        return manager;
    }

    public File getFile() {
        return file;
    }

    @Override
    public Optional<Task> createTask(Task task) {
        Optional<Task> result = super.createTask(task);
        if (!isLoad) {
            save();
        }
        return result;
    }

    @Override
    public Optional<Subtask> createSubtask(Subtask subtask) {
        Optional<Subtask> result = super.createSubtask(subtask);
        if (!isLoad) {
            save();
        }
        return result;
    }

    @Override
    public Optional<Epic> createEpic(Epic epic) {
        Optional<Epic> result = super.createEpic(epic);
        if (!isLoad) {
            save();
        }
        return result;
    }

    @Override
    public boolean deleteTask(Long id) {
        boolean result = super.deleteTask(id);
        if (result && !isLoad) {
            save();
        }
        return result;
    }

    @Override
    public Optional<Epic> updateEpic(Epic updatedEpic) {
        Optional<Epic> result = super.updateEpic(updatedEpic);
        if (!isLoad) {
            save();
        }
        return result;
    }

    @Override
    public Optional<Subtask> updateSubtask(Subtask updateSubtask) {
        Optional<Subtask> result = super.updateSubtask(updateSubtask);
        if (!isLoad) {
            save();
        }
        return result;
    }

    @Override
    public Optional<Task> updateTask(Task updatedTask) {
        Optional<Task> result = super.updateTask(updatedTask);
        if (!isLoad) {
            save();
        }
        return result;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        if (!isLoad) {
            save();
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : super.listOfAllTasks()) {
                writer.write(task.toCsv());
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл.", e);
        }
    }

    private Task fromString(String value) {
        try {
            String[] result = value.split(",");
            if (result.length < 5) {
                throw new InvalidDataFormatException("Недостаточно данных для создания задачи: " + value);
            }
            Type type;
            try {
                type = Type.valueOf(result[1]);
            } catch (IllegalArgumentException e) {
                throw new InvalidDataFormatException("Некорректный тип задачи: " + result[1], e);
            }
            return switch (type) {
                case TASK -> new Task(
                        result[2],
                        result[4],
                        Long.parseLong(result[0]),
                        Status.valueOf(result[3])
                );
                case EPIC -> new Epic(
                        result[2],
                        result[4],
                        Long.parseLong(result[0]),
                        Status.valueOf(result[3])
                );
                case SUBTASK -> {
                    if (result.length < 6) {
                        throw new InvalidDataFormatException("Недостаточно данных для создания подзадачи: " + value);
                    }
                    yield new Subtask(
                            result[2],
                            result[4],
                            Long.parseLong(result[0]),
                            Status.valueOf(result[3]),
                            Long.parseLong(result[5])
                    );
                }
            };
        } catch (NumberFormatException e) {
            throw new InvalidDataFormatException("Ошибка при разборе числового значения в строке: " + value, e);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataFormatException("Некорректный статус или другое значение в строке: " + value, e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidDataFormatException("Недостаточно данных в строке для создания задачи: " + value, e);
        }
    }
}
