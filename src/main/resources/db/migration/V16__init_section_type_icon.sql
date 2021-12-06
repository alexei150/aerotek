create table IF NOT EXISTS section_type_icon
(
    id serial primary key not null,
    section_type_id bigint not null UNIQUE,
    image_id bigint,
    icon_base_id bigint,
    icon_inflow_to_right_id bigint,
    icon_inflow_to_left_id bigint,
    icon_outflow_to_right_id bigint,
    icon_outflow_to_left_id bigint,
    foreign key (section_type_id) references section_type(id) ON DELETE CASCADE,
    foreign key (image_id) references media_content(id) ON DELETE SET NULL,
    foreign key (icon_base_id) references media_content(id) ON DELETE SET NULL,
    foreign key (icon_inflow_to_right_id) references media_content(id) ON DELETE SET NULL,
    foreign key (icon_inflow_to_left_id) references media_content(id) ON DELETE SET NULL,
    foreign key (icon_outflow_to_right_id) references media_content(id) ON DELETE SET NULL,
    foreign key (icon_outflow_to_left_id) references media_content(id) ON DELETE SET NULL,

    created_by varchar(50) not null,
    created_date timestamp not null,
    last_modified_by varchar(50) not null,
    last_modified_date timestamp not null
);