package ru.kanban.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Long> listSubtaskId = new ArrayList<>();

    public Epic(String title, String description, long id, Status progress) {
        super(title, description, id, progress);
    }

    public Epic(String title, String description, Status progress) {
        super(title, description, progress);
    }

    public void deleteSubtaskId(Long id) {
        listSubtaskId.remove(id);
    }

    public void addSubtaskId(Long id) {
        listSubtaskId.add(id);
    }

    public List<Long> getListSubtaskId() {
        return listSubtaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(listSubtaskId, epic.listSubtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listSubtaskId);
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