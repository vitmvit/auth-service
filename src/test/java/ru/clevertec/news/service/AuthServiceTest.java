package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.model.entity.User;
import ru.clevertec.news.repository.UserRepository;
import ru.clevertec.news.service.impl.AuthServiceImpl;
import ru.clevertec.news.util.AuthTestBuilder;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void signUpSuccess() {
        var signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        var expectedJwt = AuthTestBuilder.builder().build().buildJwtDto();
        var encryptedPassword = new BCryptPasswordEncoder().encode(signUpDto.getPassword());
        var newUser = new User(signUpDto.getLogin(), encryptedPassword, signUpDto.getRole());

        when(userRepository.existsByLogin(signUpDto.getLogin())).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(tokenService.buildJwt(signUpDto.getLogin(), signUpDto.getPassword())).thenReturn(expectedJwt);

        var actual = authService.signUp(signUpDto);

        assertEquals(expectedJwt, actual);
    }

    @Test
    public void signUpShouldReturnInvalidJwtException() {
        var signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();

        when(userRepository.existsByLogin(signUpDto.getLogin())).thenReturn(true);

        assertThrows(InvalidJwtException.class, () -> authService.signUp(signUpDto));
    }

    @Test
    public void signInSuccess() {
        var signInDto = AuthTestBuilder.builder().build().buildSignInDto();
        var expectedJwt = AuthTestBuilder.builder().build().buildJwtDto();

        when(userRepository.existsByLogin(signInDto.getLogin())).thenReturn(true);
        when(tokenService.buildJwt(signInDto.getLogin(), signInDto.getPassword())).thenReturn(expectedJwt);

        var actual = authService.signIn(signInDto);

        assertEquals(expectedJwt, actual);
    }

    @Test
    public void signInShouldReturnInvalidJwtException() {
        var signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        when(userRepository.existsByLogin(signInDto.getLogin())).thenReturn(false);

        assertThrows(InvalidJwtException.class, () -> authService.signIn(signInDto));
    }

    @Test
    public void checkShouldReturnTrue() {
        var token = AuthTestBuilder.builder().build().buildJwtDto().getAccessToken();

        when(tokenService.checkToken(token)).thenReturn(true);

        assertTrue(authService.check(token));
    }

    @Test
    public void checkShouldReturnFalse() {
        var token = AuthTestBuilder.builder().build().buildJwtDto().getAccessToken();

        when(tokenService.checkToken(token)).thenReturn(false);

        assertFalse(authService.check(token));
    }
}