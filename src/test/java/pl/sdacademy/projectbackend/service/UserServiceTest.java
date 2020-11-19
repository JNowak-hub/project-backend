package pl.sdacademy.projectbackend.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("test");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password");
    }

    @Test
    @DisplayName("When repository return not null Optional of User then User should be returned")
    void test1() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(testUser));
        //when
        User returnedUser = userService.findUserById(1L);
        //then
        assertThat(returnedUser).isEqualTo(testUser);

    }

    @Test
    @DisplayName("When repository return null Optional of User then UserNotFound exception is thrown")
    void test2() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.findUserById(1L));
        //then
        assertThat(exception.getMessage()).isEqualTo("User with id: " + 1 + " doesn't exists");
    }

    @Test
    @DisplayName("When repository return not null Optional of User then User should be deleted")
    void test3() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(testUser));
        //when
        userService.deleteUserById(1L);
        //then
        verify(userRepository, Mockito.times(1)).delete(testUser);
    }
}
