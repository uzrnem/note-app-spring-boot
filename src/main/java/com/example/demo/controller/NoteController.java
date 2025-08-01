package com.example.demo.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.demo.interfaces.Autheticate;
import com.example.demo.schema.NoteRequest;
import com.example.demo.schema.NoteResponse;
import com.example.demo.schema.Response;
import com.example.demo.utils.DataNotFoundException;
import com.example.demo.utils.NoteNotFoundException;
import com.example.demo.services.NoteService;

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
    private NoteService service;

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
        return ResponseEntity.ok(new Response<>("notes listed!", service.list((Integer)request.getAttribute("user_id"))));
    }

    @Autheticate
    @PostMapping()
    @Operation(summary = "Add Note to User List", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<NoteResponse>> addNote(@Valid @RequestBody NoteRequest noteRequest) throws DataNotFoundException {
        return ResponseEntity.ok(
            new Response<>(
                "note created!", 
                service.add(noteRequest, (Integer)request.getAttribute("user_id"))
            )
        );
    }

    @Autheticate
    @GetMapping("/{id}")
    @Operation(summary = "Fetch User Note", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<NoteResponse>> getNote(@PathVariable("id") Long id) throws NoteNotFoundException {

        return ResponseEntity.ok(
            new Response<>(
                "note fetched!", 
                service.get(id, (Integer)request.getAttribute("user_id"))
            )
        );
    }

    @Autheticate
    @PostMapping("/{id}")
    @Operation(summary = "Update User Note", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<NoteResponse>> updateNote(@PathVariable("id") Long id, @Valid @RequestBody NoteRequest noteRequest) throws DataNotFoundException {

        return ResponseEntity.ok(
            new Response<>(
                "note updated!", 
                service.update(id, noteRequest, (Integer)request.getAttribute("user_id"))
            )
        );
    }

    @Autheticate
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User Note", tags = { "note" }, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "successful operation"),
        @ApiResponse(responseCode = "401", description = "failed operation",
                content = @Content(schema = @Schema(implementation = Response.class))) })
    public ResponseEntity<Response<?>> deleteNote(@PathVariable("id") Long id) throws DataNotFoundException {
        service.delete(id, (Integer)request.getAttribute("user_id"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}