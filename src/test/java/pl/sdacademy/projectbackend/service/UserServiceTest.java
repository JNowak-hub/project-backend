package pl.sdacademy.projectbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.UserRepository;

import java.util.Optional;

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

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin(TEST_USER_PASSWORD);
        testUser.setEmail("test@test.com");
        testUser.setPassword("password");
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
        UserDetails returnedUser = userService.loadUserByUsername("test");
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
        verify(userRepository, Mockito.times(1)).delete(testUser);
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
        assertThat(newUser.getLogin()).isEqualTo("test");
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
        when(userRepository.findUserByEmail("test")).thenReturn(Optional.ofNullable(testUser));
        //when
        User userByEmail = userService.findUserByEmail("test");
        //then
        assertThat(userByEmail).isEqualTo(testUser);
    }

    @Test
    @DisplayName("When findUserByEmail gets null Optional of User then UsetNotFound exception is thrown")
    void test10() {
        //given
        when(userRepository.findUserByEmail("test")).thenReturn(Optional.empty());
        //when
        UserNotFound userNotFound = assertThrows(UserNotFound.class, () -> userService.findUserByEmail("test"));
        //then
        assertThat(userNotFound.getMessage()).isEqualTo("User with email: " + "test" + " doesn't exists");
    }

}
