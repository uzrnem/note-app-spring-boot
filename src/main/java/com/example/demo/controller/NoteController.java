package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.interfaces.Autheticate;
import com.example.demo.repository.NoteRepository;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    HttpServletRequest request;

    @Autheticate
    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(request.getAttribute("email"));
    }
}
