package ru.kanban.models;

import java.util.Objects;

public class Task {
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

    public Task(String title, String description, Status progress) {
        this.title = title;
        this.description = description;
        this.progress = progress;
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

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", progress=" + progress +
                '}';
    }
}
