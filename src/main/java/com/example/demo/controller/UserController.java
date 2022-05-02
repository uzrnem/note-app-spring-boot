package com.example.demo.controller;

import java.util.Optional;
import javax.validation.Valid;

import com.example.demo.entity.User;
import com.example.demo.pojo.SignupRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils util;
	
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("email exists!");
        }
        // encode password
	// String encoded = new BCryptPasswordEncoder().encode(plainTextPassword);
        User user = new User(request.getEmail(), request.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok("saved!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SignupRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("email not found!");
	}
	// compare with encoded password
	// String encoded = new BCryptPasswordEncoder().encode(plainTextPassword);
        if (user.get().getPassword().equals(request.getPassword())) {
            String jwtToken = util.generateToken(user.get().getId());
            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.ok("failed!"); //update response 
        }
    }

}
