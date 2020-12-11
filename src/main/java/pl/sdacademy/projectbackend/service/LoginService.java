package pl.sdacademy.projectbackend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.model.authentication.AuthenticationRequest;
import pl.sdacademy.projectbackend.model.authentication.AuthenticationResponse;
import pl.sdacademy.projectbackend.utilities.JwtUtil;

@Service
public class LoginService {

    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private UserService userService;

    public LoginService(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public AuthenticationResponse login(AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        return new AuthenticationResponse(
                jwtUtil.generateToken(
                        (User) userService.loadUserByUsername(
                        request.getUsername())
                )
        );
    }

    public Boolean validateToken(String token){
        return jwtUtil.validateToken(token);
    }
}
