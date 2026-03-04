create table epic (
    id serial primary key,
    foreign key (id)
        references task(id)
        on delete cascade
);