create table IF NOT EXISTS default_parameter
(
    id serial primary key not null,
    name varchar(100) not null,
    min_value varchar (50),
    max_value varchar (50),
    unit varchar (50) ,
    section_type_id bigint not null,
    is_calculating boolean not null,
    foreign key (section_type_id) references section_type(id) ON DELETE CASCADE
);

