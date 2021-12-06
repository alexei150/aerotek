create table IF NOT EXISTS element
(
    id serial primary key not null,
    name varchar(100) not null,
    brand varchar(100),
    code varchar(100),
    index varchar(100),
    cost_price float (24),
    description varchar(100),
    note varchar(255),
    drawing_code varchar(100),
    element_type_id bigint not null,
    foreign key (element_type_id) references element_type(id) ON DELETE CASCADE,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);