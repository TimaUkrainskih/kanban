package ru.kanban.models;

import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected long id;
    protected Status progress;
    protected Type type;

    public Task(String title, String description, long id, Status progress) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.progress = progress;
        this.type = Type.TASK;
    }

    public Task(String title, String description, Status progress) {
        this.title = title;
        this.description = description;
        this.progress = progress;
        this.type = Type.TASK;
    }

    public void updateStatus(Status status) {
        this.progress = status;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProgress(Status progress) {
        this.progress = progress;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && progress == task.progress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, progress);
    }

    public String toCsv() {
        return String.format("%d,%s,%s,%s,%s",
                this.getId(),
                this.getType(),
                this.getTitle(),
                this.getProgress(),
                this.getDescription());
    }
}
