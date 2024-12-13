package org.example.groenfroebackend.controller;


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



}
