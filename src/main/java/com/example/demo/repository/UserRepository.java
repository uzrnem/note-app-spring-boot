package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import com.example.demo.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}