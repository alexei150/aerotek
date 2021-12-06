create table IF NOT EXISTS installation_builder
(
    id serial primary key not null,
    name varchar(255),
    description varchar(255),
    inflow jsonb not null,
    outflow jsonb not null,
    outflow_is_up boolean not null,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);

