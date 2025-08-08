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
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class NoteGraphqlController {

    Logger logger = LogManager.getLogger(NoteController.class);

    @Autowired
    private NoteService service;

    @Autowired
    HttpServletRequest request;

    @Autheticate
    @QueryMapping("allNotes")
    public List<NoteResponse> list() {
        return this.service.list((Integer)request.getAttribute("user_id"));
    }

    @Autheticate
    @MutationMapping("createNote")
    public NoteResponse addNote(@Valid @Argument NoteRequest note) throws DataNotFoundException {
        return service.add(note, (Integer)request.getAttribute("user_id"));
    }

    @Autheticate
    @QueryMapping("getNote")
    public NoteResponse getNote(@Argument long noteId) throws NoteNotFoundException {
        return this.service.get(noteId, (Integer)request.getAttribute("user_id"));
    }

    @Autheticate
    @MutationMapping("updateNote")
    public NoteResponse updateNote(@Argument Long noteId, @Valid @Argument NoteRequest note) throws DataNotFoundException {
        return service.update(noteId, note, (Integer)request.getAttribute("user_id"));
    }

    @Autheticate
    @MutationMapping("deleteNote")
    public String deleteNote(@Argument Long noteId) throws DataNotFoundException {
        service.delete(noteId, (Integer)request.getAttribute("user_id"));
        return "deleted";
    }
}