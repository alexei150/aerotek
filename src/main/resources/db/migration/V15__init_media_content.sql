create table IF NOT EXISTS media_content
(
    id serial primary key not null,
    name varchar(100),
    description varchar(100),
    path varchar(100),

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);