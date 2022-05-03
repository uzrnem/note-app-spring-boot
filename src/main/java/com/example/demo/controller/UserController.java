package com.example.demo.controller;

import java.util.Optional;
import javax.validation.Valid;

import com.example.demo.entity.User;
import com.example.demo.schema.SignRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Utils;
import com.example.demo.schema.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "the User API")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils util;
	
    @PostMapping("/signup")
    public ResponseEntity<Response<String>> signup(@Valid @RequestBody SignRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new Response<>("email exists!", null));
        }
        User user = new User(request.getEmail(), util.encodePassword(request.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new Response<>("registration successfull!", util.generateToken(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@Valid @RequestBody SignRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("email not found!", null));
        }
        if (util.isPasswordMatches(request.getPassword(), user.get().getPassword())) {
            String jwtToken = util.generateToken(user.get().getId());
            return ResponseEntity.ok(new Response<>("login successfull!", util.generateToken(user.get())));
        } else {
            return ResponseEntity.badRequest().body(new Response<>("wrong password!", null));
        }
    }

}
