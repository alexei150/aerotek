create table IF NOT EXISTS parameter_value
(
    id serial primary key not null,
    value varchar(100) not null,
    parameter_id bigint not null,
    section_id bigint,
    element_id bigint,
    installation_id bigint,
    foreign key (parameter_id) references parameter(id) ON DELETE CASCADE,
    foreign key (section_id) references section(id) ON DELETE CASCADE,
    foreign key (element_id) references element(id) ON DELETE CASCADE,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);