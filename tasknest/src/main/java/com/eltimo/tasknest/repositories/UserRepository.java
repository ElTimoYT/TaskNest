package com.eltimo.tasknest.repositories;

import com.eltimo.tasknest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
