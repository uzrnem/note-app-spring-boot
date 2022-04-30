package com.example.demo.controller;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;

import com.example.demo.entity.User;
import com.example.demo.pojo.SignupRequest;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("email exists!");
        }
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

        String secret = "bhagyesh";
        Key key = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        if (user.get().getPassword().equals(request.getPassword())) {
            String jwtToken = Jwts.builder()
                .claim("email", request.getEmail())
                .setSubject("bhagyesh")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(10, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.ok("failed!");
        }
    }

}