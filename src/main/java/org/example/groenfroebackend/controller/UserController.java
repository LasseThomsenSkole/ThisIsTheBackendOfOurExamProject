package org.example.groenfroebackend.controller;


import org.example.groenfroebackend.model.User;
import org.example.groenfroebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/get-users")
    public ResponseEntity<String> getUsers(@RequestBody String email) {
    userService.getUserByEmail(email);
    return ResponseEntity.ok(email);
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/edit-user/{email}")
    public ResponseEntity<User> editUser (@PathVariable String email, @RequestBody User updatedUser) {
        userService.editUser(email, updatedUser);
        return ResponseEntity.ok(updatedUser);
    }


}
