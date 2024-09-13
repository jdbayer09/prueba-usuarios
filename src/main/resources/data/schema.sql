create table phones (
                        id uuid not null,
                        citycode integer not null,
                        contrycode integer not null,
                        number bigint not null,
                        user_id uuid not null,
                        primary key (id)
);

create table users (
                       id uuid not null,
                       is_active boolean not null,
                       created timestamp(6) not null,
                       email varchar(100) not null,
                       last_login timestamp(6) not null,
                       modified timestamp(6) not null,
                       name varchar(70) not null,
                       password varchar(200) not null,
                       token varchar(500) not null,
                       primary key (id)
);

alter table if exists phones
    add constraint user_unique_phone unique (number, citycode, contrycode, user_id);

alter table if exists users
    add constraint user_unique_email unique (email);

alter table if exists phones
    add constraint phone_user_id_fk
    foreign key (user_id)
    references users (id);