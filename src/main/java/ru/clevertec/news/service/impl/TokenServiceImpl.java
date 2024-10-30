package ru.clevertec.news.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.clevertec.news.config.TokenProvider;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.dto.JwtDto;
import ru.clevertec.news.repository.UserRepository;
import ru.clevertec.news.service.TokenService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    /**
     * Проверяет валидность JWT токена.
     *
     * @param token JWT токен
     * @return true, если токен валиден, иначе false
     */
    public boolean checkToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Метод для создания и возвращения JWT-токена
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return объект JwtDto, содержащий JWT-токен
     */
    public JwtDto buildJwt(String login, String password) {
        var user = userRepository.findByLogin(login).orElseThrow(EntityNotFoundException::new);
        var accessToken = tokenProvider.generateAccessToken(user);
        return JwtDto.newBuilder().setAccessToken(accessToken).build();
    }
}