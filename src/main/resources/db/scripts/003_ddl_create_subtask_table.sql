create table subtask (
    id serial primary key,
    epic_id bigint not null,
    foreign key (id)
        references task(id)
        on delete cascade,
    foreign key (epic_id)
        references epic(id)
        on delete cascade
);