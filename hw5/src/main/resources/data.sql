insert into genre values ((select nextval('seq_genre')), 'роман');
insert into genre values ((select nextval('seq_genre')), 'детектив');
insert into genre values ((select nextval('seq_genre')), 'фентези');
insert into genre values ((select nextval('seq_genre')), 'фантастика');

insert into author values ((select nextval('seq_author')), 'Лев Толстой');
insert into author values ((select nextval('seq_author')), 'Анджей Сапко́вский');
insert into author values ((select nextval('seq_author')), 'Агата Кристи');
insert into author values ((select nextval('seq_author')), 'Джон Толкин');
insert into author values ((select nextval('seq_author')), 'Виктор Пелевин');

insert into book values ((select nextval('seq_book')), 'Анна Каренина',(select author_id from author where name = 'Лев Толстой'));
insert into book values ((select nextval('seq_book')), 'Кровь эльфов',(select author_id from author where name = 'Анджей Сапко́вский'));
insert into book values ((select nextval('seq_book')), 'Убийство в Восточном экспрессе',(select author_id from author where name = 'Агата Кристи'));
insert into book values ((select nextval('seq_book')), 'Властелин колец',(select author_id from author where name = 'Джон Толкин'));
insert into book_genre
values ((select book_id from book where title = 'Анна Каренина'), (select genre_id from genre where type = 'роман'));
insert into book_genre
values ((select book_id from book where title = 'Кровь эльфов'), (select genre_id from genre where type = 'фентези'));
insert into book_genre
values ((select book_id from book where title = 'Убийство в Восточном экспрессе'), (select genre_id from genre where type = 'детектив'));
insert into book_genre
values ((select book_id from book where title = 'Властелин колец'), (select genre_id from genre where type = 'фантастика'));
