package ru.clevertec.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.model.dto.JwtDto;
import ru.clevertec.news.model.dto.SignInDto;
import ru.clevertec.news.model.dto.SignUpDto;
import ru.clevertec.news.service.impl.AuthServiceImpl;
import ru.clevertec.news.util.AuthTestBuilder;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthServiceImpl authService;

    @Test
    public void signUpSuccess() {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        JwtDto expected = AuthTestBuilder.builder().build().buildJwtDto();

        when(authService.signUp(signUpDto)).thenReturn(expected);

        var actual = authService.signUp(signUpDto);

        assertEquals(expected, actual);
    }

    @Test
    public void signUpShouldReturnInvalidJwtException() {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();

        when(authService.signUp(signUpDto)).thenThrow(InvalidJwtException.class);

        assertThrows(InvalidJwtException.class, () -> authService.signUp(signUpDto));
    }

    @Test
    public void signInSuccess() {
        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();
        JwtDto expected = AuthTestBuilder.builder().build().buildJwtDto();

        when(authService.signIn(signInDto)).thenReturn(expected);

        var actual = authService.signIn(signInDto);

        assertEquals(expected, actual);
    }

    @Test
    public void signInShouldReturnInvalidJwtException() {
        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        when(authService.signIn(signInDto)).thenThrow(InvalidJwtException.class);

        assertThrows(InvalidJwtException.class, () -> authService.signIn(signInDto));
    }

    @Test
    public void checkShouldReturnTrue() throws JsonProcessingException {
        Long userId = AuthTestBuilder.builder().build().buildUser().getId();
        String login = null;
        String token = AuthTestBuilder.builder().build().buildJwtDto().getAccessToken();

        when(authService.check(token, userId, login)).thenReturn(true);

        assertTrue(authService.check(token, userId, login));
    }

    @Test
    public void checkShouldReturnFalse() throws JsonProcessingException {
        Long userId = AuthTestBuilder.builder().build().buildUser().getId();
        String login = null;
        String token = AuthTestBuilder.builder().build().buildJwtDto().getAccessToken();

        when(authService.check(token, userId, login)).thenReturn(false);

        assertFalse(authService.check(token, userId, login));
    }
}
