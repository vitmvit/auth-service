package ru.clevertec.news.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.annotation.Log;
import ru.clevertec.news.model.dto.JwtDto;
import ru.clevertec.news.model.dto.SignInDto;
import ru.clevertec.news.model.dto.SignUpDto;
import ru.clevertec.news.service.AuthService;

import static ru.clevertec.news.constant.Constant.AUTHORIZATION_HEADER;
import static ru.clevertec.news.constant.Constant.BEARER_PREFIX;

@Log
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtDto signUp(@RequestBody @Valid SignUpDto dto) {
        return authService.signUp(dto);
    }

    @PostMapping(value = "/signIn")
    @ResponseStatus(HttpStatus.OK)
    public JwtDto signIn(@RequestBody @Valid SignInDto dto) {
        return authService.signIn(dto);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public boolean check(@RequestHeader(AUTHORIZATION_HEADER) String auth) {
        return authService.check(auth.replace(BEARER_PREFIX, ""));
    }
}