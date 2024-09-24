package ru.kanban.models;

public enum Type {
    TASK("TASK"),
    SUBTASK("SUBTASK"),
    EPIC("EPIC");

    private final String value;

    Type(final String value) {
        this.value = value;
    }

    public static Type fromValue(String value) {
        for (final Type type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
