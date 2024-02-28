package ru.clevertec.news.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.annotation.Log;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.service.AuthService;

/**
 * Контроллер для авторизации и аутентификации
 */
@Log
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Регистрация нового пользователя
     *
     * @param dto объект SignUpDto с данными для регистрации
     * @return объект ResponseEntity с JWT-токеном типа JwtDto и статусом OK
     */
    @PostMapping("/signUp")
    public ResponseEntity<JwtDto> signUp(@RequestBody @Valid SignUpDto dto) {
        return ResponseEntity.ok(authService.signUp(dto));
    }

    /**
     * Авторизация пользователя
     *
     * @param dto объект SignInDto с данными для авторизации
     * @return объект ResponseEntity с JWT-токеном типа JwtDto и статусом OK, если авторизация успешна
     */
    @PostMapping("/signIn")
    public ResponseEntity<JwtDto> signIn(@RequestBody @Valid SignInDto dto) {
        return ResponseEntity.ok(authService.signIn(dto));
    }

    /**
     * Проверка валидности JWT-токена
     *
     * @param token  JWT-токен
     * @param userId идентификатор пользователя
     * @param login  логин пользователя
     * @return true, если JWT-токен действителен; false в противном случае
     * @throws JsonProcessingException если возникает ошибка при обработке токена
     */
    @PostMapping("/check")
    public ResponseEntity<Boolean> check(@RequestParam("token") String token,
                                         @RequestParam(name = "userId", required = false) Long userId,
                                         @RequestParam(name = "login", required = false) String login) throws JsonProcessingException {
        return ResponseEntity.ok(authService.check(token, userId, login));
    }
}