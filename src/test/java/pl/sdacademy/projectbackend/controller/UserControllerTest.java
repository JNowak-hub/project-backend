package pl.sdacademy.projectbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sdacademy.projectbackend.NullableConverter;
import pl.sdacademy.projectbackend.configuration.CustomExceptionHandler;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.oauth.facebook.model.AuthProvider;
import pl.sdacademy.projectbackend.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
public class UserControllerTest {

    public static final String TEST_USER_PASSWORD = "password";
    public static final String TEST_USER_LOGIN = "login";
    public static final String TEST_USER_EMAIL = "login@login.com";
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
        testUser.setPassword(TEST_USER_PASSWORD);
        testUser.setLogin(TEST_USER_LOGIN);
        testUser.setEmail(TEST_USER_PASSWORD);
        testUser.setProvider(AuthProvider.local);
    }

    @Test
    @DisplayName("When call findUserById should return status 200 and User response body")
    public void test1() throws Exception {
        //given
        when(userService.findUserById(1L)).thenReturn(testUser);
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
        //when then
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When call addUser should return status 201")
    public void test3() throws Exception {
        //given
        when(userService.addUser(any(User.class))).thenReturn(testUser);
        String testUserJson = "{\n" +
                "  \"password\": \"" + TEST_USER_PASSWORD + "\",\n" +
                "  \"login\": \"" + TEST_USER_LOGIN + "1" + "\",\n" +
                "  \"email\": \"" + TEST_USER_EMAIL + "\",\n" +
                "  \"provider\": \"" + "local" + "\"\n" +
                "}";

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/user/add")
                        .content(testUserJson)
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", is(testUser.getLogin())));

    }

    @ParameterizedTest
    @CsvSource(value = {
            "test:test:null",
            "null:test:test@test.pl",
            "test:null:test@test.pl",
            "test:test:test",
            "t:test:test@test.pl",
            "test:t:test@test.pl"},
            delimiter = ':')
    @DisplayName("When call addUser bad request fields field should return status bad request")
    public void test4(@ConvertWith(NullableConverter.class) String password, String login, String email) throws Exception {
        //given when
        ObjectMapper objectMapper = new ObjectMapper();
        testUser.setEmail(email);
        testUser.setPassword(password);
        testUser.setLogin(login);
        ResultActions resultActions = mockMvc.perform(post("/api/user/add")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When call deleteUserById should return status 204 and verify deleteUserById Method")
    public void test5() throws Exception {
        //given when

        mockMvc
                .perform(delete("/api/user/1"))
                .andExpect(status().isNoContent());

        //then
        verify(userService).deleteUserById(1L);
    }

    @Test
    @DisplayName("When call findUserByMail should return status 200 and User response body")
    public void test6() throws Exception {
        //given
        when(userService.findUserByEmail("login@login.com")).thenReturn(testUser);
        //when
        mockMvc
                .perform(get("/api/user/by-email/login@login.com/"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", is(testUser.getLogin())));
        //then
    }

    @Test
    @DisplayName("When call findUserByFirstAndLastName should return status 200 and User response body")
    public void test7() throws Exception {
        //given
        List<User> testUsers = Arrays.asList(testUser);
        when(userService.findUserByFirstNameAndLastName("firstName", "lastName")).thenReturn(testUsers);
        //when then
        mockMvc.perform(get("/api/user/firstName/lastName"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].login", is(testUser.getLogin())));
    }

    @Test
    @DisplayName("When call findUserByFirstAndLastName should return status 404")
    public void test8() throws Exception {
        //given
        List<User> testUsers = Arrays.asList(testUser);
        when(userService.findUserByFirstNameAndLastName(anyString(), anyString())).thenThrow(UserNotFound.class);
        //when then
        mockMvc
                .perform(get("/api/user/firstName/lastName"))
                .andExpect(status().isNotFound());
    }
}
