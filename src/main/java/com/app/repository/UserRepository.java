package com.app.repository;

import com.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>findByUsername(String username);    //this is developed for donot signup if username & email already exists
    Optional<User>findByEmailId(String email);
}