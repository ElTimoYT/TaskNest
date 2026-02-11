package com.eltimo.tasknest.controllers;

import com.eltimo.tasknest.dto.UserDTO;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.repositories.UserRepository;
import com.eltimo.tasknest.services.UserService;
import com.eltimo.tasknest.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userService = new UserServiceImpl(userRepository);
    }

    @GetMapping("/users")
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public UserDTO findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/users")
    public UserDTO save(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
