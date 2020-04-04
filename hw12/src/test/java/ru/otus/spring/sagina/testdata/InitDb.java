package ru.otus.spring.sagina.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.spring.sagina.entity.*;
import ru.otus.spring.sagina.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InitDb {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void init() {
        insertAuthors(mongoTemplate);
        insertGenres(mongoTemplate);
        insertBooks(mongoTemplate);
        insertComments(mongoTemplate);
        insertUsers(mongoTemplate);
    }

    public void clean() {
        mongoTemplate.getDb().drop();
    }

    private void insertAuthors(MongoTemplate mongoTemplate) {
        mongoTemplate.insertAll(List.of(new Author("1", "Лев Толстой"),
                new Author("2", "Анджей Сапко́вский"),
                new Author("3", "Агата Кристи"),
                new Author("4", "Джон Толкин"),
                new Author("5", "Виктор Пелевин")));
    }

    private void insertGenres(MongoTemplate mongoTemplate) {
        mongoTemplate.insertAll(List.of(new Genre("1", "роман"), new Genre("2", "детектив"),
                new Genre("3", "фентези"), new Genre("4", "фантастика")));
    }

    private void insertBooks(MongoTemplate mongoTemplate) {
        Book book1 = new Book();
        book1.setId("1");
        book1.setTitle("Анна Каренина");
        book1.setDescription("ОПИСАНИЕ");
        book1.setAuthor(mongoTemplate.findById("1", Author.class));
        book1.getGenres().add(mongoTemplate.findById("1", Genre.class));
        Book book2 = new Book();
        book2.setId("2");
        book2.setTitle("Кровь эльфов");
        book2.setDescription("ОПИСАНИЕ");
        book2.setAuthor(mongoTemplate.findById("2", Author.class));
        book2.getGenres().add(mongoTemplate.findById("3", Genre.class));
        Book book3 = new Book();
        book3.setId("3");
        book3.setTitle("Убийство в Восточном экспрессе");
        book3.setDescription("ОПИСАНИЕ");
        book3.setAuthor(mongoTemplate.findById("3", Author.class));
        book3.getGenres().add(mongoTemplate.findById("2", Genre.class));
        Book book4 = new Book();
        book4.setId("4");
        book4.setTitle("Властелин колец");
        book4.setDescription("ОПИСАНИЕ");
        book4.setAuthor(mongoTemplate.findById("4", Author.class));
        book4.getGenres().add(mongoTemplate.findById("4", Genre.class));
        mongoTemplate.insertAll(List.of(book1, book2, book3, book4));
    }

    private void insertComments(MongoTemplate mongoTemplate) {
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

    public void insertUsers(MongoTemplate mongoTemplate) {
        String password = "$2a$10$BRfnmxJfwIfISHYx5F6vGOgr.LDy02ovrDs4svdfaXVaOnYQm5/zi";
        mongoTemplate.insertAll(List.of(
                new User("1", "Veronika", false, LocalDateTime.parse("2020-12-12T00:00:30"), password, UserRole.ROLE_ADMIN),
                new User("2", "Vasya", false, LocalDateTime.parse("2019-12-03T00:00:30"), password, UserRole.ROLE_ADMIN),
                new User("3", "Victor", true, LocalDateTime.parse("2020-06-03T00:00:30"), password, UserRole.ROLE_USER),
                new User("4", "Irina", false, LocalDateTime.parse("2020-04-15T00:00:30"), password, UserRole.ROLE_USER)
        ));
    }
}
