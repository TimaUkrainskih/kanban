create type task_status as enum ('NEW','IN_PROGRESS','DONE');

create type task_type as enum ('TASK','SUBTASK','EPIC');

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    progress task_status NOT NULL,
    type task_type NOT NULL
);