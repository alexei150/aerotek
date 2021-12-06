create table IF NOT EXISTS default_parameter_dictionary
(
    id serial primary key not null,
    value varchar(50),
    default_parameter_id integer,
    foreign key (default_parameter_id) references default_parameter(id) ON DELETE CASCADE
);

