create table IF NOT EXISTS acoustic_performance
(
    id serial primary key not null,
    "hz_63" float (24),
    "hz_125" float (24),
    "hz_250" float (24),
    "hz_500" float (24),
    "hz_1000" float (24),
    "hz_2000" float (24),
    "hz_4000" float (24),
    "hz_8000" float (24),
    section_id bigint UNIQUE,
    foreign key (section_id) references section(id) ON DELETE CASCADE,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);