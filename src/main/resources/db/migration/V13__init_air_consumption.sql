create table IF NOT EXISTS air_consumption
(
    id serial primary key not null,
    consumption bigint not null ,
    pressure float (24) not null ,
    section_id bigint not null ,
    foreign key (section_id) references section(id) ON DELETE CASCADE,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);