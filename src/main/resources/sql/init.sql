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
)