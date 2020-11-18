package pl.sdacademy.projectbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sdacademy.projectbackend.configuration.CustomExceptionHandler;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.service.UserService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private User testUser;

    @BeforeEach
    void mockSetup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
        testUser = new User();
        testUser.setPassword("password");
        testUser.setLogin("login");
        testUser.setEmail("login@login.com");
    }

    @Test
    @DisplayName("When call findUserById should return status 200 and User response body")
    public void test1() throws Exception {
        //given
        when(userService.findUserById(1L)).thenReturn(testUser);
        ObjectMapper objectMapper = new ObjectMapper();
        //when then
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", is(testUser.getLogin())));
    }

    @Test
    @DisplayName("When call findUserById should return status 404")
    public void test2() throws Exception {
        //given
        when(userService.findUserById(1L)).thenThrow(UserNotFound.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //when then
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }
}
