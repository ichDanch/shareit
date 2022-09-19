DROP TABLE IF EXISTS COMMENTS, BOOKINGS, REQUESTS ,USERS, ITEMS;

CREATE TABLE IF NOT EXISTS USERS
(
    id serial
        constraint users_pk
            primary key,
    name    varchar(50)        not null,
    email   varchar(50) unique not null
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    id     serial
        constraint items_pk
            primary key,
    name        varchar(50) not null,
    description varchar(200),
    available   boolean     not null,
    owner_id    bigint
        constraint items_users_id_fk
            references users,
    request_id  bigint
);
CREATE TABLE IF NOT EXISTS BOOKINGS
(
    id bigserial
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

CREATE TABLE IF NOT EXISTS COMMENTS
(
    id bigserial
        constraint comments_pk
            primary key,
    text       varchar(500),
    item_id    bigint not null
        constraint comments_items_item_id_fk
            references items,
    author_id  bigint not null
        constraint comments_users_user_id_fk
            references users,
    created    timestamp
);
CREATE TABLE IF NOT EXISTS REQUESTS
(
    id           bigserial
        constraint requests_pk
            primary key,
    description  varchar(200),
    requestor_id bigint not null
        constraint requests_users_user_id_fk
            references users,
    created      timestamp without time zone
);
