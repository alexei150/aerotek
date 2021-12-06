create table IF NOT EXISTS section_element
(
    id serial primary key not null,
    section_id bigint not null,
    element_id bigint not null,
    element_count bigint not null,

    foreign key (section_id) references section(id) ON DELETE CASCADE,
    foreign key (element_id) references element(id) ON DELETE CASCADE,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);

