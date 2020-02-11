drop table if exists book_genre cascade;
drop table if exists book_comment cascade;
drop table if exists book cascade;
drop table if exists genre cascade;
drop table if exists author cascade;

create table genre
(
    genre_id serial  primary key,
    type     varchar(255)
);

create table author
(
    author_id serial primary key,
    name      varchar(255)
);

create table book
(
    book_id   serial primary key,
    title     varchar(255),
    author_id int,
    foreign key (author_id) references author (author_id)
);

create table book_comment
(
    comment_id   serial primary key,
    message     varchar(255),
    book_id int,
    foreign key (book_id) references book (book_id)
);

create table book_genre
(
    book_id  int,
    genre_id int,
    foreign key (book_id) references book (book_id),
    foreign key (genre_id) references genre (genre_id)
)
