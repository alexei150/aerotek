CREATE TYPE selection_status AS ENUM ('BUILDER', 'AIR_APPLIED', 'CALCULATED', 'DONE', 'ARCHIVE');

create table IF NOT EXISTS installation_to_selection
(
    id serial primary key not null,
    name varchar(100),
    address varchar(100),
    inflow_pressure bigint,
    outflow_pressure bigint,
    inflow_consumption bigint,
    outflow_consumption bigint,
    inflow_temperature jsonb,
    outflow_temperature jsonb,
    standard_size smallint,
    description varchar(255),
    status selection_status,
    thickness bigint,
    summer_mode boolean,
    insertion boolean,
    outside boolean,
    winter_mode boolean,
    total_weight float (24),
    builder jsonb,
    inflow_parameters jsonb,
    outflow_parameters jsonb,
    draw_model jsonb,
    acoustic_table jsonb,
    project_id bigint,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);