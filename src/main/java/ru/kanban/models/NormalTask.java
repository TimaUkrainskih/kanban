package ru.kanban.models;

public class NormalTask extends Task{
    public NormalTask(String title, String description, long id, Status progress) {
        super(title, description, id, progress);
    }

    public void updateStatus(Status status) {
        this.progress = status;
    }
}
