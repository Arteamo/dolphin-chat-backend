create table users
(
    id                serial primary key not null,
    login             text               not null,
    password_hash     varchar(32)        not null,
    email             text               not null,
    created_timestamp timestamp          not null
);

create table tokens
(
    token   varchar(36) primary key,
    user_id serial references users (id) unique
);

create index tokens_user_id_idx on tokens (user_id);

create index users_login_idx on users (login);

select *
from users;

select *
from tokens;