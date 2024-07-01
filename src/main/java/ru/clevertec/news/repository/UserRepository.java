package ru.clevertec.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.news.model.entity.User;

/**
 * Интерфейс репозитория для работы с сущностью Пользователя
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по логину
     *
     * @param login логин пользователя
     * @return найденный пользователь или null, если пользователь не найден
     */
    User findByLogin(String login);

    /**
     * Проверить, существует ли пользователь с указанным логином
     *
     * @param login логин пользователя
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByLogin(String login);
}