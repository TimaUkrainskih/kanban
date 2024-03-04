package ru.kanban.models;

import java.util.List;

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
}