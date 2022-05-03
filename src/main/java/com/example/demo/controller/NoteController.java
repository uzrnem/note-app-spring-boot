package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.demo.interfaces.Autheticate;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.schema.NoteRequest;
import com.example.demo.schema.Response;
import com.example.demo.entity.Note;
import com.example.demo.entity.User;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/notes")
@Tag(name = "note", description = "the Note API")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpServletRequest request;

    @Autheticate
    @GetMapping()
    @Operation(summary = "Get all notes for user", description = "Return list of notes", tags = { "note" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<List<Note>>> list() {

        List<Note> list = noteRepository.getByUserId((Integer)request.getAttribute("user_id"));
        return ResponseEntity.ok(new Response<>("Success", list));
    }

    @Autheticate
    @PostMapping()
    @Operation(summary = "Add Note to List", description = "create note", tags = { "note" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<Note>> add(@Valid @RequestBody NoteRequest noteRequest) {

        Optional<User> user = userRepository.findById((Integer)request.getAttribute("user_id"));
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("email not found!", null));
        }
        Note note = new Note(noteRequest.getContent(), user.get(), noteRequest.getIsCompleted());
        noteRepository.save(note);
        return ResponseEntity.ok(new Response<>("Success", note));
    }

    @Autheticate
    @GetMapping("/{id}")
    @Operation(summary = "Get note by id", description = "Return note of user", tags = { "note" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<Note>> get(@PathVariable("id") Integer id) {

        Optional<Note> notePromise = noteRepository.getByIdAndUserId(id, (Integer)request.getAttribute("user_id"));
        if (!notePromise.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("note not found!", null));
        }
        Note note = notePromise.get();
        return ResponseEntity.ok(new Response<>("Success", note));
    }

    @Autheticate
    @PostMapping("/{id}")
    @Operation(summary = "Update note by id", description = "Update note of user", tags = { "note" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<Note>> update(@PathVariable("id") Integer id, @Valid @RequestBody NoteRequest noteRequest) {

        Optional<Note> notePromise = noteRepository.getByIdAndUserId(id, (Integer)request.getAttribute("user_id"));
        if (!notePromise.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("note not found!", null));
        }
        Note note = notePromise.get();
        note.setContent(noteRequest.getContent());
        note.setIsCompleted(noteRequest.getIsCompleted());
        noteRepository.save(note);
        return ResponseEntity.ok(new Response<>("Success", note));
    }

    @Autheticate
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete note by id", description = "Delete note of user", tags = { "note" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {

        Optional<Note> notePromise = noteRepository.getByIdAndUserId(id, (Integer)request.getAttribute("user_id"));
        if (!notePromise.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("note not found!", null));
        }
        Note note = notePromise.get();
        noteRepository.delete(note);
        return ResponseEntity.ok().body(new Response<>("no content!", null));
    }
}
