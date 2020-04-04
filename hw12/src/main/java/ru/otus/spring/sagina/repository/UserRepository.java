package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.sagina.entity.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(String login);
}
