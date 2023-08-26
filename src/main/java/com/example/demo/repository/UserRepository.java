package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.demo.entity.User;

@Repository 
public interface UserRepository extends CrudRepository<User, Integer> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}