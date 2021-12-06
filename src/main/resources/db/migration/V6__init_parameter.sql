create table IF NOT EXISTS parameter
(
    id serial primary key not null,
    name varchar(100) not null UNIQUE,
    min_value varchar (50),
    max_value varchar (50),
    unit varchar (50),
    element_type_id bigint,
    section_type_id bigint,
    foreign key (element_type_id) references element_type(id) ON DELETE CASCADE,
    foreign key (section_type_id) references section_type(id) ON DELETE CASCADE,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);

