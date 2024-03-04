package ru.clevertec.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.news.dto.auth.SignInDto;
import ru.clevertec.news.dto.auth.SignUpDto;
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.service.AuthService;
import ru.clevertec.news.util.AuthTestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void signUpSuccess() throws Exception {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void signUpShouldReturnInvalidJwtException() throws Exception {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();

        String signUpDtoJson = objectMapper.writeValueAsString(signUpDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDtoJson))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }

    @Test
    public void signInSuccess() throws Exception {
        SignUpDto signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        when(authService.signUp(any())).thenReturn(AuthTestBuilder.builder().build().buildJwtDto());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void signInSuccessShouldReturnInvalidJwtException() throws Exception {
        SignInDto signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto)))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }

    @Test
    public void checkShouldReturnStatus200() throws Exception {
        Long userId = AuthTestBuilder.builder().build().buildUser().getId();
        String login = null;
        String token = AuthTestBuilder.builder().build().buildJwtDto().accessToken();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/check")
                        .param("token", token)
                        .param("userId", String.valueOf(userId))
                        .param("login", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
