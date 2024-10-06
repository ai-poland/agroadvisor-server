package com.aipol.agroadvisor.controller;

import com.aipol.agroadvisor.model.User;
import com.aipol.agroadvisor.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/db/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> newUser(@RequestBody User user) {
        return this.userService.newUser(user);
    }

    @GetMapping("/")
    public List<User> getUsers() {
        return this.userService.getUsers();
    }

    @GetMapping("/{login}")
    public ResponseEntity<Object> getUserByLogin(@PathVariable String login) {
        return this.userService.getUserByLogin(login);
    }
}
