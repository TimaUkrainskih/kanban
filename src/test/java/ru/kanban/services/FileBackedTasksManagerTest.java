package ru.kanban.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.kanban.exception.ManagerSaveException;
import ru.kanban.models.Epic;
import ru.kanban.models.Status;
import ru.kanban.models.Subtask;
import ru.kanban.models.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class FileBackedTasksManagerTest {

    private FileBackedTasksManager manager;
    private Task task;
    private Subtask subtask;
    private Epic epic;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        File source = tempDir.resolve("target.txt").toFile();
        manager = new FileBackedTasksManager(new InMemoryHistoryManager(), source);
        task = new Task("title1", "description", Status.NEW);
        subtask = new Subtask("title1", "description", Status.NEW, 2L);
        epic = new Epic("title1", "description", Status.NEW);
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
    }

    @Test
    void recoverDataFromFile() {
        FileBackedTasksManager targetManager = FileBackedTasksManager.loadFromFile(manager.getFile(), new InMemoryHistoryManager());
        assertThat(targetManager.listOfAllTasks()).hasSameElementsAs(manager.listOfAllTasks());
    }

    @Test
    void recoverEpicAndSubtasks() {
        epic.addSubtaskId(subtask.getId());
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(manager.getFile(), new InMemoryHistoryManager());
        Epic loadedEpic = (Epic) loadedManager.findById(epic.getId()).orElseThrow();
        assertThat(loadedEpic.getListSubtaskId()).contains(subtask.getId());
    }

    @Test
    void recoverEmptyFile(@TempDir Path tempDir) {
        File emptyFile = tempDir.resolve("target.txt").toFile();
        try {
            emptyFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать пустой файл для теста", e);
        }
        FileBackedTasksManager emptyManager = FileBackedTasksManager.loadFromFile(emptyFile, new InMemoryHistoryManager());
        assertThat(emptyManager.listOfAllTasks()).isEmpty();
        assertThat(emptyManager.getHistory()).isEmpty();
    }

    @Test
    void recoverInvalidData(@TempDir Path tempDir) {
        File file = tempDir.resolve("target.txt").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Empty\n");
            writer.write("Empty\n");
            writer.write("Empty\n");
            writer.write("Empty\n");
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка записи в файл во время подготовки теста", e);
        }
        assertThatThrownBy(() -> FileBackedTasksManager.loadFromFile(file, new InMemoryHistoryManager()))
                .isInstanceOf(ManagerSaveException.class)
                .message()
                .isEqualTo("Ошибка при обработке данных.");
    }

    @Test
    void recoverInvalidFile(@TempDir Path tempDir) {
        File file = tempDir.resolve("target.txt").toFile();
        assertThatThrownBy(() -> FileBackedTasksManager.loadFromFile(file, new InMemoryHistoryManager()))
                .isInstanceOf(ManagerSaveException.class)
                .message()
                .isEqualTo("Ошибка при загрузке из файла.");
    }
}
