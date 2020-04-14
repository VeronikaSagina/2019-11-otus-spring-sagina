drop table if exists book_genre cascade;
drop table if exists book_comment cascade;
drop table if exists book cascade;
drop table if exists genre cascade;
drop table if exists author cascade;

create table if not exists author
(
    author_id serial   primary key,
    name   varchar(255)
);

create table if not exists genre
(
    genre_id serial   primary key,
    name   varchar(255)
);

create table if not exists book
(
    book_id   serial primary key,
    name   varchar(255),
    author_id   int,
    description   text,
    foreign key (author_id) references author (author_id)
);

create table if not exists book_comment
(
    comment_id   serial primary key,
    message   varchar(255),
    book_id   int,
    foreign key (book_id) references book (book_id)
);

create table if not exists book_genre
(
    book_id   int,
    genre_id   int,
    foreign key (book_id) references book (book_id),
    foreign key (genre_id) references genre (genre_id)
);

create table if not exists user
(
     user_id   serial primary key,
     login   varchar(255),
     locked   boolean default false,
     password   varchar(255),
     role   varchar(255)
);

/*-----------------------------------acl-schema-----------------------------------------*/

create table if not exists acl_sid
(
    id        bigint(20) not null auto_increment,
    principal tinyint(1)   not null,
    sid       varchar(100) not null,
    primary key (id),
    unique key unique_uk_1 (sid,principal)
);

create table if not exists acl_class
(
    id    bigint(20) not null auto_increment,
    class varchar(255) not null,
    primary key (id),
    unique key unique_uk_2 (class)
);

create table if not exists acl_entry
(
    id                  bigint(20) not null auto_increment,
    acl_object_identity bigint(20) not null,
    ace_order           int(11) not null,
    sid                 bigint(20) not null,
    mask                int(11) not null,
    granting            tinyint(1) not null,
    audit_success       tinyint(1) not null,
    audit_failure       tinyint(1) not null,
    primary key (id),
    unique key unique_uk_4 (acl_object_identity,ace_order)
);

create table if not exists acl_object_identity
(
    id                 bigint(20) not null auto_increment,
    object_id_class    bigint(20) not null,
    object_id_identity bigint(20) not null,
    parent_object      bigint(20) default null,
    owner_sid          bigint(20) default null,
    entries_inheriting tinyint(1) not null,
    primary key (id),
    unique key unique_uk_3 (object_id_class,object_id_identity)
);

alter table acl_entry
    add foreign key (acl_object_identity) references acl_object_identity (id);

alter table acl_entry
    add foreign key (sid) references acl_sid (id);

alter table acl_object_identity
    add foreign key (parent_object) references acl_object_identity (id);

alter table acl_object_identity
    add foreign key (object_id_class) references acl_class (id);

alter table acl_object_identity
    add foreign key (owner_sid) references acl_sid (id);
