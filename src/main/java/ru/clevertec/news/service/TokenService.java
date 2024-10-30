package ru.clevertec.news.service;

import ru.clevertec.news.model.dto.JwtDto;

public interface TokenService {

    boolean checkToken(String token);

    JwtDto buildJwt(String login, String password);
}