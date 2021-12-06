create table IF NOT EXISTS section_type
(
    id serial primary key not null,
    name varchar(100) not null UNIQUE,
    code varchar(3) not null UNIQUE,
    note varchar(100),

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);