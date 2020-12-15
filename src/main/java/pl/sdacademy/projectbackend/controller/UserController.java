package pl.sdacademy.projectbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.service.UserService;
import pl.sdacademy.projectbackend.validaiton.groups.StandardUserValidation;

import javax.validation.groups.Default;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User");
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@Validated({StandardUserValidation.class, Default.class}) @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    // trailing slash to enable dot as parameter char
    @GetMapping(path = "/by-email/{email}/")
    public ResponseEntity<User> getUserByMail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping("/{firstname}/{lastname}")
    public ResponseEntity<List<User>> getUserByFullName(@PathVariable String firstname, @PathVariable String lastname){
        return ResponseEntity.ok(userService.findUserByFirstNameAndLastName(firstname, lastname));
    }

    @GetMapping("/{token}")
    public ResponseEntity<User> getUserByToken(@PathVariable String token){
        return ResponseEntity.ok(userService.findUserByToken(token));
    }
}
