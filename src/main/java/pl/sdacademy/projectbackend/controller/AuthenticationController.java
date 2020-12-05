package pl.sdacademy.projectbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.projectbackend.model.authentication.AuthenticationRequest;
import pl.sdacademy.projectbackend.model.authentication.AuthenticationResponse;
import pl.sdacademy.projectbackend.service.LoginService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("login")
public class AuthenticationController {
    @Value("${urlfailredirect.allUri}")
    private String redirectFailUri;
    @Value("${urlCorrectlyRedirect.allUri}")
    private String correctlyRedirect;
    private LoginService loginService;

    public AuthenticationController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(loginService.login(request));
    }

    @GetMapping("/verifyToken")
    public ResponseEntity<Void> isTokenValid(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("{error}")
    public ResponseEntity<String> failFacebookAuthentication(@Valid @PathVariable String error, HttpServletResponse response) {
        try {
            response.sendRedirect(redirectFailUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
    }

    @GetMapping()
    public ResponseEntity<String> authenticateFacebookUser(@Valid @RequestParam String token, HttpServletResponse response) {
        response.setHeader("Authorization", "Bearer " + token);

        try {
            response.sendRedirect(correctlyRedirect + token);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
