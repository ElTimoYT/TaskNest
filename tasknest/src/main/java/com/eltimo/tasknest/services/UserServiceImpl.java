package com.eltimo.tasknest.services;

import com.eltimo.tasknest.dto.TaskDTO;
import com.eltimo.tasknest.dto.UserDTO;
import com.eltimo.tasknest.entities.Tag;
import com.eltimo.tasknest.entities.User;
import com.eltimo.tasknest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return convertirADTO(user);
    }

    @Override
    @Transactional
    public UserDTO save(User user) {
        User userToSave = userRepository.save(user);
        return convertirADTO(userToSave);
    }

    @Override
    public UserDTO updateProfile(User currentUser, UserDTO changes) {
        // 1. Solo actualizamos campos permitidos
        if (changes.getName() != null) {
            currentUser.setName(changes.getName());
        }

        if (changes.getEmail() != null) {
            currentUser.setEmail(changes.getEmail());
        }

        if (changes.getUsername() != null) {
            currentUser.setUsername(changes.getUsername());
        }

        currentUser.setNotificationsEnabled(changes.isNotificationEnabled());

        User savedUser = userRepository.save(currentUser);

        return convertirADTO(savedUser);
    }

    @Override
    @Transactional
    public Optional<UserDTO> update(Long id, UserDTO userDTO) {
        Optional<User> userDTOOptional = userRepository.findById(id);
        if (userDTOOptional.isPresent()) {
            User userToUpdate = userDTOOptional.get();
            userToUpdate.setName(userDTO.getName());
            userToUpdate.setEmail(userDTO.getEmail());
            userToUpdate.setUsername(userDTO.getUsername());

            User updatedUser = userRepository.save(userToUpdate);
            return Optional.of(convertirADTO(updatedUser));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        userRepository.delete(user);
    }



    public UserDTO convertirADTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());

        if (user.getTasks() != null) {
            userDTO.setTasks(user.getTasks().stream()
                    .map(task -> new TaskDTO(
                            task.getUuid(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getNotes(),
                            task.getPriority(),
                            task.getState(),
                            task.getUser().getId(),
                            task.getDueDate(),
                            task.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
                    ))
                    .toList());
        }
        return userDTO;
    }
}
