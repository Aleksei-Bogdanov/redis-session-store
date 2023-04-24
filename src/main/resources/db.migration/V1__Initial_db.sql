create table tbl_messages (
                        id bigserial primary key,
                        message varchar(1024),
                        person_id bigserial
)