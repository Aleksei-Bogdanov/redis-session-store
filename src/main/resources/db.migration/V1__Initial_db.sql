create table if not exists tbl_messages (
    id bigserial primary key,
    message varchar(1024),
    person_id bigserial
);

create table if not exists tbl_users (
    id bigserial primary key,
    username varchar(64),
    password varchar(64),
    user_role varchar(64)
);