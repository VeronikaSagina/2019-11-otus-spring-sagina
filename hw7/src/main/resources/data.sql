insert into genre (type) values ('роман');
insert into genre (type) values ('детектив');
insert into genre (type) values ('фентези');
insert into genre (type) values ('фантастика');

insert into author (name) values ('Лев Толстой');
insert into author (name) values ('Анджей Сапко́вский');
insert into author (name) values ('Агата Кристи');
insert into author (name) values ('Джон Толкин');
insert into author (name) values ('Виктор Пелевин');

insert into book (title, author_id) values ('Анна Каренина',(select author_id from author where name = 'Лев Толстой'));
insert into book (title, author_id) values ('Кровь эльфов',(select author_id from author where name = 'Анджей Сапко́вский'));
insert into book (title, author_id) values ('Убийство в Восточном экспрессе',(select author_id from author where name = 'Агата Кристи'));
insert into book (title, author_id) values ('Властелин колец',(select author_id from author where name = 'Джон Толкин'));

insert into book_comment (message, book_id) values ('очень хорошая книга стоит почитать', (select book_id from book where title = 'Анна Каренина'));
insert into book_comment (message, book_id) values ('this is my favorite russian book!', (select book_id from book where title = 'Анна Каренина'));
insert into book_comment (message, book_id) values ('не тратьте время', (select book_id from book where title = 'Убийство в Восточном экспрессе'));
insert into book_comment (message, book_id) values ('фильм лучше!', (select book_id from book where title = 'Властелин колец'));
insert into book_comment (message, book_id) values ('прочитала за вечер, рекомендую', (select book_id from book where title = 'Властелин колец'));

insert into book_genre
values ((select book_id from book where title = 'Анна Каренина'), (select genre_id from genre where type = 'роман'));
insert into book_genre
values ((select book_id from book where title = 'Кровь эльфов'), (select genre_id from genre where type = 'фентези'));
insert into book_genre
values ((select book_id from book where title = 'Убийство в Восточном экспрессе'), (select genre_id from genre where type = 'детектив'));
insert into book_genre
values ((select book_id from book where title = 'Властелин колец'), (select genre_id from genre where type = 'фантастика'));
