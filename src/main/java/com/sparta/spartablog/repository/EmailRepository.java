package com.sparta.spartablog.repository;

import com.sparta.spartablog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String email);
}





