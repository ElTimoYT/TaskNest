package com.eltimo.tasknest.controllers;

import com.eltimo.tasknest.dto.ChangePasswordDTO;
import com.eltimo.tasknest.dto.UserDTO;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.repositories.UserRepository;
import com.eltimo.tasknest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.eltimo.tasknest.utils.ValidationUtils.validation;

@RestController
@RequestMapping("/api/users")
@Tag(name="User Controller", description="Endpoints for managing users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.convertirADTO(user));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordDTO dto,
            @AuthenticationPrincipal User user) {

        // 1. Comprobamos que la contraseña actual sea correcta
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "La contraseña actual es incorrecta"));
        }

        // 2. Encriptamos la nueva y la guardamos
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "Contraseña actualizada con éxito"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Updates the user with the specified ID using the provided information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user data"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> update(@Valid @RequestBody UserDTO userDTO, @AuthenticationPrincipal User user,  BindingResult result) {
        if (result.hasErrors()) {
            if (result.getFieldError() != null){
                return validation(result);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", result.getFieldError().getDefaultMessage()));
        }
        UserDTO updatedUser = userService.updateProfile(user, userDTO);
        return ResponseEntity.ok(updatedUser);
    }


}
