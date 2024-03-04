package ru.kanban.models;

public abstract class Task {
    protected String title;
    protected String description;
    protected long id;
    protected Status progress;

    public Task(String title, String description, long id, Status progress) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.progress = progress;
    }

    public Status getProgress() {
        return progress;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
