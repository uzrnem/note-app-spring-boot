package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.demo.entity.User;

@Repository 
public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByEmail(String email);

    //@Query(value = "SELECT * FROM User u WHERE u.email=:email", nativeQuery = true)
    Optional<User> findByEmail(String email);
}