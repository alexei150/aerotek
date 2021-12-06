create table IF NOT EXISTS parameter_element_type
(
    id serial primary key not null,
    parameter_id bigint,
    element_type_id bigint,
    foreign key (parameter_id) references parameter(id) ON DELETE CASCADE,
    foreign key (element_type_id) references element_type(id) ON DELETE CASCADE
);