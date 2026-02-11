package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.UserDTO;
import com.eltimo.tasknest.entities.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(@NotNull Long id);
    UserDTO save(User user);
    void deleteById(Long id);
}
