package pl.sdacademy.projectbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.Role;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.model.authentication.AuthenticationRequest;
import pl.sdacademy.projectbackend.model.authentication.AuthenticationResponse;
import pl.sdacademy.projectbackend.repository.EventRepository;
import pl.sdacademy.projectbackend.utilities.JwtUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;

    @InjectMocks
    private LoginService loginService;

    private AuthenticationRequest authenticationRequest;
    private UserDetails userDetails;
    private User user;
    @BeforeEach
    void setUp(){
        user = new User();
        user.setLogin("login");
        user.setPassword("password");
        user.setRole(Role.USER);
        userDetails = user;

        authenticationRequest = new AuthenticationRequest("login", "password");
    }

    @Test
    @DisplayName("When login then return JWT")
    public void test1(){
        //given
        when(userService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn("generated.token");
        //when
        AuthenticationResponse response = loginService.login(authenticationRequest);
        //then
        assertThat("generated.token").isEqualTo(response.getJwt());
    }

    @Test
    @DisplayName("When login then throw BadCredentialsException")
    public void test2(){
        //given
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        //when
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> loginService.login(authenticationRequest));
        //then
        verify(userService, never()).loadUserByUsername(any());
        verify(jwtUtil, never()).generateToken(any());
    }


}
