package pl.sdacademy.projectbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sdacademy.projectbackend.NullableConverter;
import pl.sdacademy.projectbackend.exceptions.BadRequestException;
import pl.sdacademy.projectbackend.exceptions.UserAlreadyExists;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.UserRepository;
import pl.sdacademy.projectbackend.utilities.JwtUtil;
import pl.sdacademy.projectbackend.utilities.SecurityContextUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    public static final String TEST_USER_PASSWORD = "password";
    public static final String TEST_USER_LOGIN = "test";
    public static final String TEST_USER_EMAIL = "test@test.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private SecurityContextUtils contextUtils;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin(TEST_USER_LOGIN);
        testUser.setEmail(TEST_USER_EMAIL);
        testUser.setPassword(TEST_USER_PASSWORD);
    }

    @Test
    @DisplayName("When findUserById gets not null Optional of User then User should be returned")
    void test1() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(testUser));
        //when
        User returnedUser = userService.findUserById(1L);
        //then
        assertThat(returnedUser).isEqualTo(testUser);
    }

    @Test
    @DisplayName("When findUserById gets null Optional of User then UsetNotFound exception is thrown")
    void test2() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.findUserById(1L));
        //then
        assertThat(exception.getMessage()).isEqualTo("User with id: " + 1 + " doesn't exists");
    }

    @Test
    @DisplayName("When findUserByUserName gets not null Optional of User then returns user")
    void test3() {
        //given
        when(userRepository.findUserByLogin(testUser.getLogin())).thenReturn(Optional.of(testUser));
        //when
        UserDetails returnedUser = userService.loadUserByUsername(TEST_USER_LOGIN);
        //then
        assertThat(returnedUser.getUsername()).isEqualTo(testUser.getLogin());
    }

    @Test
    @DisplayName("When findUserByUserName gets null Optional of User then throws UserNotFound")
    void test4() {
        //given
        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.empty());
        //when
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.loadUserByUsername("wrong login"));
        //then
        assertThat(exception.getMessage()).isEqualTo("User with login: " + "wrong login" + " doesn't exists");
    }

    @Test
    @DisplayName("When repository return not null Optional of User then User should be deleted")
    void test5() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(testUser));
        //when
        userService.deleteUserById(1L);
        //then
        verify(userRepository).delete(testUser);
    }

    @Test
    @DisplayName("When repository gets null Optional of User then User then throws UserNotFound")
    void test6() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        UserNotFound userNotFound = assertThrows(UserNotFound.class, () -> userService.deleteUserById(1l));
        //then
        assertThat(userNotFound.getMessage()).isEqualTo("User with id: " + 1 + " doesn't exists");
    }
    @Test
    @DisplayName("Should add user to userRepository")
    void test7() {
        //given
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        //when
        User newUser = userRepository.save(new User());
        //then
        assertThat(newUser.getLogin()).isEqualTo(testUser.getLogin());
    }

    @Test
    @DisplayName("Should not add user to userRepository")
    void test8() {
        //given
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        //when
        User newUser = userRepository.save(new User());
        //then
        assertThat(newUser.getLogin()).isNotEqualTo(testUser.getLogin() + 1);
    }

    @Test
    @DisplayName("When findUserByEmail gets not null Optional of User then returns user")
    void test9() {
        //given
        when(userRepository.existsByLogin(testUser.getLogin())).thenReturn(true);
        //when
        UserAlreadyExists exception = assertThrows(UserAlreadyExists.class, () -> userService.addUser(testUser));
        //then
        assertThat("User with login: " + testUser.getLogin() + " already exists").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("When findUserByEmail gets not null Optional of User then returns user")
    void test10() {
        //given

        when(userRepository.findUserByEmail("test")).thenReturn(Optional.ofNullable(testUser));
        //when
        User userByEmail = userService.findUserByEmail("test");
        //then
        assertThat(userByEmail).isEqualTo(testUser);
    }

    @Test
    @DisplayName("When findUserByEmail gets null Optional of User then UsetNotFound exception is thrown")
    void test11() {
        //given
        when(userRepository.findUserByEmail("test")).thenReturn(Optional.empty());
        //when
        UserNotFound userNotFound = assertThrows(UserNotFound.class, () -> userService.findUserByEmail("test"));
        //then
        assertThat(userNotFound.getMessage()).isEqualTo("User with email: " + "test" + " doesn't exists");
    }

    @Test
    @DisplayName("When findUserByFirstNameAndLastName is called then returns User")
    void test12() {
        //given
        List<User> testUsers = Arrays.asList(testUser);
        when(userRepository.findUserByFirstNameAndLastName("firstName","lastName")).thenReturn(testUsers);
        //when
        List<User> userByFirstNameAndLastName = userService.findUserByFirstNameAndLastName("firstName", "lastName");
        //then
        assertThat(userByFirstNameAndLastName).isEqualTo(testUsers);
    }

    @Test
    @DisplayName("When findUserByFirstNameAndLastName is called then returns empty list")
    void test13() {
        //given
        List<User> testUsers = Collections.emptyList();
        when(userRepository.findUserByFirstNameAndLastName(anyString(),anyString())).thenReturn(testUsers);
        //when
        List<User> userByFirstNameAndLastName = userService.findUserByFirstNameAndLastName("firstName", "lastName");
        //then
        assertThat(userByFirstNameAndLastName).isNotSameAs(testUsers);
    }

    @Test
    @DisplayName("When find user by token then return found user")
    void test14() {
        //given
        String token = "token.token.token";
        when(jwtUtil.extractEmail(token)).thenReturn(testUser.getEmail());
        when(userRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(contextUtils.getCurrentUser()).thenReturn(testUser);
        when(jwtUtil.validateToken(token,testUser)).thenReturn(true);
        //when
        User user = userService.findUserByToken(token);
        //then
        assertThat(testUser).isEqualTo(user);
    }

    @ParameterizedTest()
    @ValueSource(strings = {
            "",
            " ",
            "   ",
            "null"
    })
    @DisplayName("When find user by empty or null token then throw BadRequestException exception")
    void test15(@ConvertWith(NullableConverter.class) String tokenValue) {
        //given
        String token = tokenValue;
        //when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.findUserByToken(token));
        //then
        assertThat("Token can not be empty or null").isEqualTo(exception.getMessage());
    }


    @Test
    @DisplayName("When find user by token when no user in context holder then throw UserNotFound exception")
    void test17() {
        //given
        String token = "token.token.token";
        when(contextUtils.getCurrentUser()).thenThrow(UserNotFound.class);
        //when
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.findUserByToken(token));
        //then
        verify(jwtUtil, never()).extractEmail(token);
        verify(userRepository, never()).findUserByEmail(any());
    }

    @Test
    @DisplayName("When find user by invalid token then throw BadRequestException exception")
    void test18() {
        //given
        String token = "token.token.token";
        when(contextUtils.getCurrentUser()).thenReturn(testUser);
        when(jwtUtil.validateToken(token,testUser)).thenReturn(false);
        //when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.findUserByToken(token));
        //then
        assertThat("Invalid token!").isEqualTo(exception.getMessage());
    }
}
