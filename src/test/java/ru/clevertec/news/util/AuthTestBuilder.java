package ru.clevertec.news.util;

import lombok.Builder;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.dto.constant.RoleName;
import ru.clevertec.news.model.entity.User;

@Builder(setterPrefix = "with")
public class AuthTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String login = "Admin111";

    @Builder.Default
    private String password = "Admin";

    @Builder.Default
    private RoleName role = RoleName.ADMIN;


    public SignUpDto buildSignUpDto() {
        return new SignUpDto(login, password, role);
    }

    public SignInDto buildSignInDto() {
        return new SignInDto(login, password);
    }

    public JwtDto buildJwtDto() {
        return new JwtDto("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBZG1pbiIsInVzZXJuYW1lIjoiQWRtaW4iLCJyb2xlIjoiQURNSU4iLCJleHAiOjE3MDkxNjY1NjZ9.26SzqC817uuNXtk6TsGp6ga0HW_GM9HlGBAsoXW9tr0");
    }

    public User buildUser() {
        return new User(id, login, password, role);
    }
}
