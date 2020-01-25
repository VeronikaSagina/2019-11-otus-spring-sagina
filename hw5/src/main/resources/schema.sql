drop table if exists book_genre;
drop table if exists genre;
drop table if exists author;
drop table if exists book;

create table genre
(
    genre_id int primary key,
    type     varchar(255)
);
create sequence seq_genre;

create table author
(
    author_id int primary key,
    name      varchar(255)
);
create sequence seq_author;

create table book
(
    book_id   int primary key,
    title     varchar(255),
    author_id int,
    foreign key (author_id) references author (author_id)
);
create sequence seq_book;

create table book_genre
(
    book_id  int,
    genre_id int,
    foreign key (book_id) references book (book_id),
    foreign key (genre_id) references genre (genre_id)
)
