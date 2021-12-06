create table IF NOT EXISTS section
(
    id serial primary key not null,
    name varchar(100) not null,
    drawing_code varchar(100) not null UNIQUE,
    description varchar(100),
    note varchar(255),
    build_coefficient float (24),
    hardware_coefficient float (24),
    section_area float (24),
    standard_size smallint not null,
    section_type_id bigint not null,
    foreign key (section_type_id) references section_type(id) ON DELETE CASCADE,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);