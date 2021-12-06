CREATE TYPE role AS ENUM ('ADMIN', 'USER', 'EDITOR', 'TECHNICAL_DIR', 'COMMERCIAL_DIR', 'MANAGER');

CREATE TYPE status AS ENUM ('ACTIVE', 'BANNED');

create table IF NOT EXISTS employees
(
    id serial primary key not null,
    email varchar(50) UNIQUE not null,
    password varchar(100) not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    patronymic varchar(50),
    company varchar(50),
    phone_number varchar(20),
    role role not null,
    status status not null,
    created_date timestamp,
    last_modified_date timestamp,
    users int not null

    );

--функция для преобразования типов к int
create function try_cast_int(p_in text, p_default int default null)
    returns int
as
    $$
begin
begin
return $1::int;
exception
        when others then
            return p_default;
end;
end;
$$
language plpgsql;