create table IF NOT EXISTS roenest_heat_exchanger
(
    id serial primary key not null,
    mode smallint not null,
    code varchar(55) not null,
    geometry bigint not null,
    length bigint not null,
    height bigint not null,
    num_rows bigint not null,
    tubes_type bigint not null,
    fin_spacing bigint not null,
    fin_type bigint not null,
    circuits_type bigint not null,
    num_circuits bigint not null,
    header_configuration bigint not null,
    header_value_1 bigint not null,
    header_value_2 bigint not null,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);


