package pl.sdacademy.projectbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.sdacademy.projectbackend.model.Role;
import pl.sdacademy.projectbackend.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private CustomAuthenticationService authenticationService;

    private Authentication authentication;
    private User user;

    @BeforeEach
    void setup(){
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        user = new User();
        user.setPassword(passwordEncoder.encode("password"));
        user.setLogin("login");
        user.setRole(Role.USER);
        authentication = new UsernamePasswordAuthenticationToken("login", "password");
    }

    @Test
    @DisplayName("When authenticate then return authentication")
    public void test1(){
        //given
        when(userService.loadUserByUsername(user.getLogin())).thenReturn(user);
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);
        //when
        Authentication returnedAuthentication = authenticationService.authenticate(authentication);
        //then
        assertThat(user.getPassword()).isEqualTo(passwordEncoder.encode("password"));
    }

    @Test
    @DisplayName("When authenticate then throw BadCredential exception")
    public void test2(){
        //given
        when(userService.loadUserByUsername(user.getLogin())).thenReturn(user);
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(false);
        //when
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(authentication));
        //then
        assertThat("Bad credentials").isEqualTo(exception.getMessage());
    }
}
