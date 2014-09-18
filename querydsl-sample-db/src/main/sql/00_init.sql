create table author (
    id bigint primary key
    ,name varchar(512)
);

create table book (
    isbn            varchar(64) primary key
    ,title          varchar(512)
    ,author_id      bigint
    ,publisher_id   bigint
    ,foreign key (author_id) references author (id)
);

