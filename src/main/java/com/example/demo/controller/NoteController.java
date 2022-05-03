package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.interfaces.Autheticate;
import com.example.demo.repository.NoteRepository;
import com.example.demo.schema.Response;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/notes")
@Tag(name = "note", description = "the Note API")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    HttpServletRequest request;

    @Autheticate
    @GetMapping("/list")
    @Operation(summary = "Get all notes for user", description = "Return list of notes", tags = { "note" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(request.getAttribute("user"));
    }
}
