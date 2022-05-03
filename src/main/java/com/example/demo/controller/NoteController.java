package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.example.demo.interfaces.Autheticate;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.schema.NoteRequest;
import com.example.demo.schema.NoteResponse;
import com.example.demo.schema.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/note")
@Tag(name = "note", description = "the Note API")
public class NoteController {

    Logger logger = LogManager.getLogger(NoteController.class);

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpServletRequest request;

    @Autheticate
    @GetMapping()
    @Operation(summary = "List of user notes", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
            content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<List<NoteResponse>>> listNote() {
        Optional<User> user = userRepository.findById((Integer)request.getAttribute("user_id"));

        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("user not found!", null));
        }

        List<Note> notes = noteRepository.getByUserId(user.get().getId());

        List<NoteResponse> notesR = notes.stream()
            .map(n -> new NoteResponse(n.getId().intValue(), n.getContent(), n.getIsCompleted())).collect(Collectors.toList());

        return ResponseEntity.ok(new Response<>("notes listed!", notesR));
    }

    @Autheticate
    @PostMapping()
    @Operation(summary = "Add Note to User List", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<NoteResponse>> addNote(@Valid @RequestBody NoteRequest noteRequest) {
        Optional<User> user = userRepository.findById((Integer)request.getAttribute("user_id"));

        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("user not found!", null));
        }
        Note note = new Note(noteRequest.getContent(), user.get(), noteRequest.getIsCompleted());

        noteRepository.save(note);
        return ResponseEntity.ok(new Response<>("note created!", new NoteResponse(note.getId().intValue(), note.getContent(), note.getIsCompleted())));
    }

    @Autheticate
    @GetMapping("/{id}")
    @Operation(summary = "Fetch User Note", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<NoteResponse>> getNote(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById((Integer)request.getAttribute("user_id"));

        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("user not found!", null));
        }

        Optional<Note> note = noteRepository.getByIdAndUserId(id, user.get().getId());

        if (!note.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("note not found!", null));
        }

        Note n = note.get();

        return ResponseEntity.ok(new Response<>("note showed!", new NoteResponse(n.getId().intValue(), n.getContent(), n.getIsCompleted())));
    }

    @Autheticate
    @PostMapping("/{id}")
    @Operation(summary = "Update User Note", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<NoteResponse>> updateNote(@PathVariable("id") Long id, @Valid @RequestBody NoteRequest noteRequest) {
        Optional<User> user = userRepository.findById((Integer)request.getAttribute("user_id"));

        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("user not found!", null));
        }

        Optional<Note> noteOpt = noteRepository.getByIdAndUserId(id, user.get().getId());

        if (!noteOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("note not found!", null));
        }

        Note note = noteOpt.get();

        note.setContent(noteRequest.getContent());
        note.setIsCompleted(noteRequest.getIsCompleted());
        noteRepository.save(note);

        return ResponseEntity.ok(new Response<>("note updated!", new NoteResponse(note.getId().intValue(), note.getContent(), note.getIsCompleted())));
    }

    @Autheticate
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User Note", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<?>> deleteNote(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById((Integer)request.getAttribute("user_id"));

        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("user not found!", null));
        }

        Optional<Note> noteOpt = noteRepository.getByIdAndUserId(id, user.get().getId());

        if (!noteOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new Response<>("note not found!", null));
        }

        Note note = noteOpt.get();
        noteRepository.delete(note);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
