create table if not exists "user"
(
    id bigserial not null
        constraint user_pk
        primary key,
    email varchar(254),
    username varchar(255),
    password varchar(60),
    first_name varchar(255),
    last_name varchar(255),
    provider varchar(255),
    status varchar(255),
    created_at timestamp,
    modified_at timestamp
);

alter table "user" owner to postgres;

create table if not exists role
(
    id bigserial not null
        constraint role_pk
        primary key,
    name varchar(255) not null,
    created_at timestamp default current_timestamp,
    modified_at timestamp default current_timestamp
);

alter table role owner to postgres;

create table if not exists user_roles
(
    id bigserial not null
        constraint user_roles_pk
        primary key,
    user_id bigint not null
        constraint user_roles_user_fk
        references "user",
    role_id bigint not null
        constraint user_roles_role_fk
        references role
);

alter table user_roles owner to postgres;

insert into role (name) values ('USER');
