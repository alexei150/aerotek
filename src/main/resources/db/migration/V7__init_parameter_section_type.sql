create table IF NOT EXISTS parameter_section_type
(
    id serial primary key not null,
    parameter_id bigint,
    section_type_id bigint,
    foreign key (parameter_id) references parameter(id) ON DELETE CASCADE,
    foreign key (section_type_id) references section_type(id) ON DELETE CASCADE
);