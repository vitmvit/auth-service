package ru.clevertec.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;

public interface AuthService {

    JwtDto signUp(SignUpDto dto);

    JwtDto signIn(SignInDto dto);

    boolean check(String token, Long userId, String login) throws JsonProcessingException;
}
