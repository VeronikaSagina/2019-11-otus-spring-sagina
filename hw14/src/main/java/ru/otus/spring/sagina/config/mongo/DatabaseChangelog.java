package ru.otus.spring.sagina.config.mongo;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.sagina.model.Author;
import ru.otus.spring.sagina.model.Book;
import ru.otus.spring.sagina.model.BookComment;
import ru.otus.spring.sagina.model.Genre;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "001", id = "addAuthors", author = "v")
    public void insertAuthors(MongoTemplate mongoTemplate) {
        mongoTemplate.insertAll(List.of(new Author("1", "Лев Толстой"),
                new Author("2", "Анджей Сапко́вский"),
                new Author("3", "Агата Кристи"),
                new Author("4", "Джон Толкин"),
                new Author("5", "Виктор Пелевин")));
    }

    @ChangeSet(order = "002", id = "addGenres", author = "v")
    public void insertGenres(MongoTemplate mongoTemplate) {
        mongoTemplate.insertAll(List.of(new Genre("1", "роман"), new Genre("2", "детектив"),
                new Genre("3", "фентези"), new Genre("4", "фантастика")));
    }

    @ChangeSet(order = "003", id = "addBooks", author = "v")
    public void insertBooks(MongoTemplate mongoTemplate) {
        Book book1 = new Book();
        book1.setId("1");
        book1.setName("Анна Каренина");
        book1.setDescription(getAnnaKareninaDescription());
        book1.setAuthor(mongoTemplate.findById("1", Author.class));
        book1.getGenres().add(mongoTemplate.findById("1", Genre.class));
        Book book2 = new Book();
        book2.setId("2");
        book2.setName("Кровь эльфов");
        book2.setDescription(getBloodOfElvesDescription());
        book2.setAuthor(mongoTemplate.findById("2", Author.class));
        book2.getGenres().add(mongoTemplate.findById("3", Genre.class));
        Book book3 = new Book();
        book3.setId("3");
        book3.setName("Убийство в \"Восточном экспрессе\"");
        book3.setDescription(getOrientExpressDescription());
        book3.setAuthor(mongoTemplate.findById("3", Author.class));
        book3.getGenres().add(mongoTemplate.findById("2", Genre.class));
        book3.getGenres().add(mongoTemplate.findById("1", Genre.class));
        Book book4 = new Book();
        book4.setId("4");
        book4.setName("Властелин колец");
        book4.setDescription(getForLordOfTheRingDescription());
        book4.setAuthor(mongoTemplate.findById("4", Author.class));
        book4.getGenres().add(mongoTemplate.findById("4", Genre.class));
        book4.getGenres().add(mongoTemplate.findById("3", Genre.class));
        book4.getGenres().add(mongoTemplate.findById("1", Genre.class));
        mongoTemplate.insertAll(List.of(book1, book2, book3, book4));
    }

    @ChangeSet(order = "004", id = "addComments", author = "v")
    public void insertComments(MongoTemplate mongoTemplate) {
        mongoTemplate.insertAll(List.of(
                new BookComment(
                        "1", "очень хорошая книга стоит почитать", mongoTemplate.findById("1", Book.class)),
                new BookComment(
                        "2", "this is my favorite russian book!", mongoTemplate.findById("1", Book.class)),
                new BookComment(
                        "3", "не тратьте время", mongoTemplate.findById("3", Book.class)),
                new BookComment(
                        "4", "фильм лучше!", mongoTemplate.findById("4", Book.class)),
                new BookComment(
                        "5", "прочитала за вечер, рекомендую", mongoTemplate.findById("4", Book.class))));
    }

    private String getAnnaKareninaDescription() {
        return "«Анну Каренину» Толстой называл «романом широким и свободным»." +
                " В основе этого определения пушкинский термин «свободный роман»." +
                " Не фабульная завершенность положений, а творческая концепция определяет" +
                " выбор материала и открывает простор для развития сюжетных линий." +
                " Роман «широкого дыхания» привлекал Толстого тем, что в «просторную," +
                " вместительную раму» без напряжения входило все то новое," +
                " необычайное и нужное, что он хотел сказать людям.";
    }

    private String getBloodOfElvesDescription() {
        return "Цинтра захвачено Нильфгаардской империей. Всюду пламя и разрушения, сотни погибших." +
                " Прекрасное королевство пало. Наследнице Цири чудом удается спастись." +
                " Напуганную, потерявшую близких и дом девочку Геральт доставляет в убежище ведьмаков." +
                " Неожиданно для всех у принцессы открываются магические способности." +
                " Чтобы понять их природу, Геральт обращается за помощью к чародейке." +
                " Однако она советует ведьмаку призвать свою бывшую возлюбленную Йеннифэр." +
                " Ибо только она сможет научить девочку пользоваться ее даром…";
    }

    private String getForLordOfTheRingDescription() {
        return "Кто может оспорить ошеломляющую популярность этой книги во всем мире. И не случайно именно" +
                " \"Властелин Колец\" стал своего рода каноном, положившим начало новому жанру" +
                " в мире литературы, - фэнтези. Удивительный, сказочный, полный опасностей и приключений," +
                " находящийся под вечным противостоянием добра и зла, этот мир в то же время так похож на наш," +
                " что никто не остается равнодушным к Средиземью и героям, его населяющим." +
                " На долю маленького, добродушного хоббита Фродо выпала участь пройти через все" +
                " Средиземье в Темные земли Мордора и уничтожить Кольцо Всевластья," +
                " которое обладает чудовищной, разрушительной силой. Фродо помогают его друзья," +
                " среди которых хоббиты, могущественный Серый Маг, отважный гном, ловкий" +
                " эльф и многие другие, но им противостоит Саурон - Властелин темных сил," +
                " который жаждет заполучить кольцо, а вместе с ним власть над всем Средиземьем.";
    }

    private String getOrientExpressDescription() {
        return "Находившийся в Стамбуле великий сыщик Эркюль Пуаро возвращается в Англию на знаменитом" +
                " «Восточном экспрессе», в котором вместе с ним едут, кажется, представители" +
                " всех возможных национальностей. Один из пассажиров, неприятный американец по фамилии" +
                " Рэтчетт, предлагает Пуаро стать его телохранителем, поскольку считает, что его" +
                " должны убить. Знаменитый бельгиец отмахивается от этой абсурдной просьбы." +
                " А на следующий день американца находят мертвым в своем купе, причем двери закрыты," +
                " а окно открыто. Пуаро немедленно берется за расследование – и выясняет," +
                " что купе полно всевозможных улик, указывающих… практически на всех пассажиров" +
                " «Восточного экспресса». Вдобавок поезд наглухо застревает в снежных заносах в безлюдном месте." +
                " Пуаро необходимо найти убийцу до того, как экспресс продолжит свой путь…";
    }
}
