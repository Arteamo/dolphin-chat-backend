create table users
(
    id                serial primary key not null,
    username          text               not null,
    password_hash     varchar(32)        not null,
    email             text unique        not null,
    created_timestamp timestamp          not null
);

create table tokens
(
    token   varchar(36) primary key,
    user_id serial references users (id) unique
);

create table rooms
(
    id            serial primary key not null,
    title         text               not null,
    encoded_image text default null
);

create table message_types
(
    id    serial primary key not null,
    title text               not null
);

create type message_types as enum ('user_msg', 'user_joined', 'user_left');

create table messages
(
    id             serial primary key           not null,
    msg_text       text                         not null,
    msg_type       text                         not null,
    send_timestamp timestamp                    not null,
    sender_id      serial references users (id) not null,
    room_id        serial references rooms (id) not null
);

create table users_to_rooms
(
    user_id serial references users (id) not null,
    room_id serial references rooms (id) not null,
    unique (user_id, room_id)
);

create index user_id_idx_utr on users_to_rooms (user_id);
create index room_id_idx_utr on users_to_rooms (room_id);

alter table users
    add column encoded_image text null;


alter table messages
    add column encoded_data text null;

select * from rooms;
