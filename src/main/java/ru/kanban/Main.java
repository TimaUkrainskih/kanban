package ru.kanban;

import ru.kanban.models.Task;
import ru.kanban.services.FileBackedTasksManager;
import ru.kanban.services.InMemoryHistoryManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file1 = new File("test.txt");
//        FileBackedTasksManager manager = new FileBackedTasksManager(new InMemoryHistoryManager(), file1);
//        Task task = new Task("title1", "description", Status.NEW);
//        Subtask subtask = new Subtask("title1", "description", Status.NEW, 2L);
//        Epic epic = new Epic("title1", "description", Status.NEW);
//        manager.createTask(task);
//        manager.createEpic(epic);
//        manager.createSubtask(subtask);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file1, new InMemoryHistoryManager());
        for (Task task1 : fileBackedTasksManager.listOfAllTasks()) {
            System.out.println(task1.toCsv());
        }
    }
}