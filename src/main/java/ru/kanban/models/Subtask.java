package ru.kanban.models;

import java.util.Objects;

public class Subtask extends Task {
    private Long epicId;

    public Subtask(String title, String description, long id, Status progress, Long epicId) {
        super(title, description, id, progress);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status progress, Long epicId) {
        super(title, description, progress);
        this.epicId = epicId;
    }

    public void updateStatus(Status status) {
        this.progress = status;
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d",
                this.getId(),
                Type.SUBTASK,
                this.getTitle(),
                this.getProgress(),
                this.getDescription(),
                this.epicId);
    }
}
