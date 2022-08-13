DROP TABLE IF EXISTS BOOKINGS, USERS, ITEMS;

CREATE TABLE IF NOT EXISTS USERS
(
    user_id    serial
        constraint users_pk
            primary key,
    name  varchar(50) not null,
    email varchar(50) unique not null
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    item_id          serial
        constraint items_pk
            primary key,
    name        varchar(50) not null,
    description varchar(200),
    available   boolean     not null,
    owner_id       bigint
        constraint items_users_id_fk
            references users,
    request_id  bigint
);

create table if not exists bookings
(
    booking_id bigserial
        constraint bookings_pk
            primary key,
    start_date timestamp not null,
    end_date   timestamp not null,
    item_id    bigint
        constraint bookings_items_id_fk
            references items,
    booker_id  bigint
        constraint bookings_users_id_fk
            references users,
    status     varchar(50)
);