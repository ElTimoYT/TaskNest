package com.eltimo.tasknest.controllers;

import com.eltimo.tasknest.dto.UserDTO;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/page/{page}")
    public Page<UserDTO> findAll(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<UserDTO> user = Optional.ofNullable(userService.findById(id));
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "The user was not found by id: " + id));
    }

    @PostMapping()
    public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            if (result.getFieldError() != null){
                return validation(result);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", result.getFieldError().getDefaultMessage()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            if (result.getFieldError() != null){
                return validation(result);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", result.getFieldError().getDefaultMessage()));
        }
        Optional<UserDTO> updatedUser = userService.update(id, userDTO);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.orElseThrow());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "The user was not found by id: " + id));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<UserDTO> user = Optional.ofNullable(userService.findById(id));
        if (user.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "The user was not found by id: " + id));
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> errors.put(err.getField(), "The field " + err.getField() + " " + err.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
