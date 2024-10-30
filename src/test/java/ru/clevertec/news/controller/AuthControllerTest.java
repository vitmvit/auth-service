package ru.clevertec.news.controller;

import com.googlecode.protobuf.format.JsonFormat;
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
import ru.clevertec.news.exception.InvalidJwtException;
import ru.clevertec.news.service.AuthService;
import ru.clevertec.news.util.AuthTestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.AUTHORIZATION_HEADER;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void signUpSuccess() throws Exception {
        var signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonFormat().printToString(signUpDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void signUpShouldReturnInvalidJwtException() throws Exception {
        var signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        var signUpDtoJson = new JsonFormat().printToString(signUpDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDtoJson))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }

    @Test
    public void signInSuccess() throws Exception {
        var signUpDto = AuthTestBuilder.builder().build().buildSignUpDto();
        var signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonFormat().printToString(signUpDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        when(authService.signUp(any())).thenReturn(AuthTestBuilder.builder().build().buildJwtDto());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonFormat().printToString(signInDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void signInSuccessShouldReturnInvalidJwtException() throws Exception {
        var signInDto = AuthTestBuilder.builder().build().buildSignInDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonFormat().printToString(signInDto)))
                .andExpect(MvcResult::getResolvedException).getClass().equals(InvalidJwtException.class);
    }

    @Test
    public void checkShouldReturnStatus200() throws Exception {
        var token = AuthTestBuilder.builder().build().buildJwtDto().getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/check")
                        .header(AUTHORIZATION_HEADER, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}