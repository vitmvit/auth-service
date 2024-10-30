package ru.clevertec.news.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.model.dto.JwtDto;
import ru.clevertec.news.model.dto.SignInDto;
import ru.clevertec.news.model.dto.SignUpDto;
import ru.clevertec.news.model.entity.User;
import ru.clevertec.news.repository.UserRepository;
import ru.clevertec.news.service.AuthService;
import ru.clevertec.news.service.TokenService;

import static ru.clevertec.news.constant.Constant.USERNAME_IS_EXIST;
import static ru.clevertec.news.constant.Constant.USERNAME_NOT_EXIST;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    /**
     * Метод для регистрации нового пользователя
     *
     * @param dto объект SignUpDto, содержащий данные для регистрации
     * @return объект JwtDto, содержащий JWT-токен
     */
    @Transactional
    public JwtDto signUp(SignUpDto dto) {
        if (userRepository.existsByLogin(dto.getLogin())) {
            log.error("AuthService: Invalid Jwt with message: " + USERNAME_IS_EXIST);
            throw new InvalidJwtException(USERNAME_IS_EXIST);
        }
        log.info("AuthService: check sign up");
        var encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        var newUser = new User(dto.getLogin(), encryptedPassword, dto.getRole());
        userRepository.save(newUser);
        return tokenService.buildJwt(dto.getLogin(), dto.getPassword());
    }

    /**
     * Метод для аутентификации пользователя
     *
     * @param dto объект SignInDto, содержащий данные для аутентификации
     * @return объект JwtDto, содержащий JWT-токен
     */
    @Override
    public JwtDto signIn(SignInDto dto) {
        if (!userRepository.existsByLogin(dto.getLogin())) {
            log.error("AuthService: Invalid Jwt with message: " + USERNAME_NOT_EXIST);
            throw new InvalidJwtException(USERNAME_NOT_EXIST);
        }
        log.info("AuthService: check sign in");
        return tokenService.buildJwt(dto.getLogin(), dto.getPassword());
    }

    /**
     * Проверяет действительность токена.
     *
     * <p>Метод использует сервис токенов для проверки подлинности переданного токена.
     * Возвращает {@code true}, если токен действителен, иначе возвращает {@code false}.</p>
     *
     * @param token токен, который требуется проверить
     * @return {@code true}, если токен действителен; {@code false} в противном случае
     */
    @Override
    public boolean check(String token) {
        log.info("AuthService: check token");
        return tokenService.checkToken(token);
    }
}