package ru.kanban;

import ru.kanban.models.Epic;
import ru.kanban.models.Status;
import ru.kanban.models.Subtask;
import ru.kanban.models.Task;
import ru.kanban.services.FileBackedTasksManager;
import ru.kanban.services.InMemoryHistoryManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file1 = new File("test.txt");
        FileBackedTasksManager manager = new FileBackedTasksManager(new InMemoryHistoryManager(), file1);
        Task task = new Task("title1", "description", Status.NEW);
        Subtask subtask = new Subtask("title3", "description", Status.NEW, 2L);
        Epic epic = new Epic("title2", "description", Status.NEW);
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createTask(new Task("title4", "description", Status.NEW));
        manager.createTask(new Task("title5", "description", Status.NEW));
        manager.deleteTask(1L);
        manager.deleteTask(2L);
        manager.deleteTask(3L);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file1, new InMemoryHistoryManager());
        for (Task task1 : fileBackedTasksManager.listOfAllTasks()) {
            System.out.println(task1.toCsv());
        }
    }
}