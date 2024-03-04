package ru.kanban.models;

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
}
