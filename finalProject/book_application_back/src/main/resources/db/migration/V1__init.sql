create table if not exists author
(
    author_id uuid primary key,
    name      text
);

create table if not exists book
(
    book_id     uuid primary key,
    name        text,
    author_id   uuid references author (author_id),
    description text

);

create table if not exists genre
(
    genre_id uuid primary key,
    name     text
);

create table if not exists book_genre
(
    book_id  uuid references book (book_id),
    genre_id uuid references genre (genre_id)
);

create table if not exists app_user
(
    user_id  uuid primary key,
    login    text,
    email    text,
    locked   bool,
    password text,
    role     text
);

create table if not exists book_comment
(
    comment_id uuid primary key,
    book_id    uuid references book (book_id),
    message    text
);

create table if not exists minio_file_meta
(
    minio_file_id uuid primary key,
    book_id       uuid references book (book_id),
    file_name     text,
    bucket_name   text,
    content_type  text
)