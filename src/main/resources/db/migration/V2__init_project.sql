create table IF NOT EXISTS project
(
    id serial primary key not null,
    name varchar(100) not null,
    address varchar(100) not null,
    company varchar(55),
    manager varchar(55),

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);