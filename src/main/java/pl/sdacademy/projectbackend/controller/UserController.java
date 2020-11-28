package pl.sdacademy.projectbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.service.UserService;
import pl.sdacademy.projectbackend.validaiton.groups.StandardUserValidation;

import javax.validation.Valid;
import javax.validation.groups.Default;

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
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@Validated({StandardUserValidation.class, Default.class}) @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

}
