package ru.clevertec.news.service;

import ru.clevertec.news.model.dto.JwtDto;
import ru.clevertec.news.model.dto.SignInDto;
import ru.clevertec.news.model.dto.SignUpDto;

public interface AuthService {

    JwtDto signUp(SignUpDto dto);

    JwtDto signIn(SignInDto dto);

    boolean check(String token);
}