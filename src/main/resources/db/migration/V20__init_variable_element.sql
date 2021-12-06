create table IF NOT EXISTS variable_element
(
    id serial primary key not null,
    section_id bigint not null,
    section_type_id bigint not null,
    standard_size smallint not null,
    order_key bigint not null,
    index varchar(100) not null
);