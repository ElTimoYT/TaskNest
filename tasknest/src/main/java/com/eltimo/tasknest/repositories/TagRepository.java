package com.eltimo.tasknest.repositories;

import com.eltimo.tasknest.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // Buscar una etiqueta por nombre Y usuario
    Optional<Tag> findByNameAndUserId(String name, Long userId);

    // Devolver todas las etiquetas de un usuario (para un selector en el frontend)
    List<Tag> findByUserId(Long userId);
}
