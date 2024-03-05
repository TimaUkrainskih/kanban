package ru.kanban.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Subtask> subtaskList;

    public Epic(String title, String description, long id, Status progress, List<Subtask> subtaskList) {
        super(title, description, id, progress);
        this.subtaskList = subtaskList;
    }

    public void updateStatus() {
        long doneCount = subtaskList.stream().filter(task -> task.getProgress() == Status.DONE).count();
        long newCount = subtaskList.stream().filter(task -> task.getProgress() == Status.NEW).count();
        if (subtaskList.isEmpty() || newCount == subtaskList.size()) {
            this.progress = Status.NEW;
        } else if (doneCount == subtaskList.size()) {
            this.progress = Status.DONE;
        } else {
            this.progress = Status.IN_PROGRESS;
        }
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskList, epic.subtaskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskList=" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", progress=" + progress +
                '}';
    }
}