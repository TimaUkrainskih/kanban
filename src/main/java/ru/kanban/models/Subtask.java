package ru.kanban.models;

import java.util.Objects;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String title, String description, long id, Status progress, Epic epic) {
        super(title, description, id, progress);
        this.epic = epic;
    }

    public void updateStatus(Status status) {
        this.progress = status;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epic, subtask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic=" + epic +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", progress=" + progress +
                '}';
    }
}
