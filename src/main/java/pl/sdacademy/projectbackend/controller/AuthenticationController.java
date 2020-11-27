package pl.sdacademy.projectbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.sdacademy.projectbackend.model.authentication.AuthenticationRequest;
import pl.sdacademy.projectbackend.model.authentication.AuthenticationResponse;
import pl.sdacademy.projectbackend.service.LoginService;

@RestController
public class AuthenticationController {

    private LoginService loginService;

    public AuthenticationController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(loginService.login(request));
    }
}
