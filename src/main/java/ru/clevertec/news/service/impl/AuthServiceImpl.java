package ru.clevertec.news.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.config.TokenProvider;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.dto.constant.RoleName;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.model.entity.TokenPayload;
import ru.clevertec.news.model.entity.User;
import ru.clevertec.news.repository.UserRepository;
import ru.clevertec.news.service.AuthService;
import ru.clevertec.news.util.StringUtils;

import java.util.Base64;

import static ru.clevertec.news.constant.Constant.*;

/**
 * Реализация сервиса аутентификации
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    /**
     * Конструктор класса AuthServiceImpl.
     *
     * @param userRepository        репозитория пользователей
     * @param tokenProvider         провайдер токенов
     * @param authenticationManager менеджер аутентификации
     */
    public AuthServiceImpl(UserRepository userRepository,
                           TokenProvider tokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Метод для регистрации нового пользователя
     *
     * @param dto объект SignUpDto, содержащий данные для регистрации
     * @return объект JwtDto, содержащий JWT-токен
     */
    @Transactional
    public JwtDto signUp(SignUpDto dto) {
        if (userRepository.existsByLogin(dto.login())) {
            throw new InvalidJwtException(USERNAME_IS_EXIST);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User newUser = new User(dto.login(), encryptedPassword, dto.role());
        userRepository.save(newUser);
        return buildJwt(dto.login(), dto.password());
    }

    /**
     * Метод для аутентификации пользователя
     *
     * @param dto объект SignInDto, содержащий данные для аутентификации
     * @return объект JwtDto, содержащий JWT-токен
     */
    @Override
    public JwtDto signIn(SignInDto dto) {
        if (!userRepository.existsByLogin(dto.login())) {
            throw new InvalidJwtException(USERNAME_NOT_EXIST);
        }
        return buildJwt(dto.login(), dto.password());
    }

    /**
     * Проверка прав доступа пользователя по JWT токену.
     *
     * @param token  JWT токен
     * @param userId идентификатор пользователя
     * @param login  логин пользователя
     * @return true, если у пользователя есть доступ, иначе false
     * @throws JsonProcessingException если возникла ошибка при обработке JSON
     */
    @Override
    public boolean check(String token, Long userId, String login) throws JsonProcessingException {
        if (checkToken(token)) {
            User user;
            if (userId != null) {
                user = userRepository.findById(userId).get();
            } else if (StringUtils.isNotEmpty(login)) {
                user = userRepository.findByLogin(login);
            } else {
                throw new NoAccessError();
            }
//            var role = user.getRole().getRole();
//            var roleToken = getRole(token);
//            if (role.equals(roleToken) && RoleName.ADMIN.equals(role)) {
//                return true;
//            }
//            var loginUser = user.getLogin();
//            var loginToken = getUsername(token);
//            return role.equals(roleToken) && loginUser.equals(loginToken);
            return (user.getRole().getRole().equals(getRole(token)) || user.getRole().getRole().equals(RoleName.ADMIN.getRole())) && user.getLogin().equals(getUsername(token));
        }
        throw new InvalidJwtException(INVALID_TOKEN_ERROR);
    }

    /**
     * Проверяет валидность JWT токена.
     *
     * @param token JWT токен
     * @return true, если токен валиден, иначе false
     */
    private boolean checkToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
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
     * Получает роль пользователя из JWT токена
     *
     * @param token JWT токен
     * @return роль пользователя
     * @throws JsonProcessingException если возникла ошибка при обработке JSON
     */
    private String getRole(String token) throws JsonProcessingException {
        String[] chinks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chinks[1]));
        TokenPayload tokenPayload = objectMapper.readValue(payload, TokenPayload.class);
        return tokenPayload.getRole();
    }

    /**
     * Получает имя пользователя из JWT токена
     *
     * @param token JWT токен
     * @return имя пользователя
     * @throws JsonProcessingException если возникла ошибка при обработке JSON
     */
    private String getUsername(String token) throws JsonProcessingException {
        String[] chinks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chinks[1]));
        TokenPayload tokenPayload = objectMapper.readValue(payload, TokenPayload.class);
        return tokenPayload.getUsername();
    }

    /**
     * Метод для создания и возвращения JWT-токена
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return объект JwtDto, содержащий JWT-токен
     */
    private JwtDto buildJwt(String login, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        var authUser = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenProvider.generateAccessToken((User) authUser.getPrincipal());
        return new JwtDto(accessToken);
    }
}