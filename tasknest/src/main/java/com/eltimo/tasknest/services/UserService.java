package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.UserDTO;
import com.eltimo.tasknest.entities.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserService {
    Page<UserDTO> findAll(Pageable pageable);
    UserDTO findById(@NotNull Long id);
    UserDTO save(User user);
    Optional<UserDTO> update(Long id, UserDTO userDTO);
    void deleteById(Long id);
    UserDTO updateProfile(User currentUser, UserDTO changes);
    UserDTO convertirADTO(User user); // Asegúrate de que este sea público en la interfaz
}
