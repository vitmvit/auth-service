package ru.clevertec.news.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.config.TokenProvider;
import ru.clevertec.news.model.entity.User;
import ru.clevertec.news.util.AuthTestBuilder;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest extends PostgresSqlContainerInitializer {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private TokenProvider tokenProvider;

    @Test
    void findByLoginShouldReturnExpectedUserDetails() {
        User user = AuthTestBuilder.builder().build().buildUser();

        userRepository.save(user);

        var actual = userRepository.findByLogin(user.getLogin());

        assertNotNull(actual);
        assertEquals(user.getLogin(), actual.getUsername());
    }

    @Test
    void existsByLoginShouldReturnTrue() {
        User user = AuthTestBuilder.builder().build().buildUser();

        userRepository.save(user);

        var actual = userRepository.existsByLogin(user.getLogin());

        assertTrue(actual);
    }

    @Test
    void existsByLoginShouldReturnFalse() {
        User user = AuthTestBuilder.builder().build().buildUser();

        var actual = userRepository.existsByLogin(user.getLogin());

        assertFalse(actual);
    }
}
