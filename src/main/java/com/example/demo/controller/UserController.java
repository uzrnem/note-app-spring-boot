package com.example.demo.controller;

import java.util.Optional;
import javax.validation.Valid;

import com.example.demo.entity.User;
import com.example.demo.schema.SignRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Utils;
import com.example.demo.schema.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils util;
	
    @Operation(summary = "Register User", tags = { "user" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation",
                content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    @PostMapping("/signup")
    public ResponseEntity<Response<String>> signup(@Valid @RequestBody SignRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            logger.warn("registration request failed for "+ request.getEmail());
            return ResponseEntity.badRequest().body(new Response<>("email exists!", null));
        }
        User user = new User(request.getEmail(), util.encodePassword(request.getPassword()));
        userRepository.save(user);
        logger.info("registration request passed for "+ request.getEmail());
        return ResponseEntity.ok(new Response<>("registration successfull!", util.generateToken(user)));
    }

    @Operation(summary = "Login User", tags = { "user" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation",
                content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@Valid @RequestBody SignRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (!user.isPresent()) {
            logger.warn("login request failed for "+ request.getEmail());
            return ResponseEntity.badRequest().body(new Response<>("email not found!", null));
        }
        if (util.isPasswordMatches(request.getPassword(), user.get().getPassword())) {
            logger.info("login request passed for "+ request.getEmail());
            return ResponseEntity.ok(new Response<>("login successfull!", util.generateToken(user.get())));
        } else {
            logger.warn("login request failed for "+ request.getEmail() + " [wrong password]");
            return ResponseEntity.badRequest().body(new Response<>("wrong password!", null));
        }
    }
}
