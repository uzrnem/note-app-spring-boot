package com.example.demo.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.schema.NoteRequest;
import com.example.demo.schema.NoteResponse;
import com.example.demo.utils.DataNotFoundException;
import com.example.demo.utils.NoteNotFoundException;
import com.example.demo.utils.UserNotFoundException;
import com.example.demo.services.NoteService;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public NoteServiceImpl() {
    }

    @Override
    public List<NoteResponse> list(Integer userId) {
        List<Note> notes = noteRepository.getByUserId(userId);

        List<NoteResponse> notesR = notes.stream()
            .map(n -> new NoteResponse(n.getId().intValue(), n.getContent(), n.getIsCompleted())).collect(Collectors.toList());

        return notesR;
    }

    @Override
    public NoteResponse add(NoteRequest noteRequest, Integer userId) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }
        Note note = new Note(noteRequest.getContent(), user.get(), noteRequest.getIsCompleted());

        noteRepository.save(note);
        return new NoteResponse(note.getId().intValue(), note.getContent(), note.getIsCompleted());
    }

    @Override
    public NoteResponse get(Long id, Integer userId) throws NoteNotFoundException {
        Optional<Note> note = noteRepository.getByIdAndUserId(id, userId);

        if (!note.isPresent()) {
            throw new NoteNotFoundException();
        }

        Note n = note.get();

        return new NoteResponse(n.getId().intValue(), n.getContent(), n.getIsCompleted());
    }

    @Override
    public NoteResponse update(Long id, NoteRequest noteRequest, Integer userId) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }

        Optional<Note> noteOpt = noteRepository.getByIdAndUserId(id, userId);

        if (!noteOpt.isPresent()) {
            throw new NoteNotFoundException();
        }

        Note note = noteOpt.get();
        note.setContent(noteRequest.getContent());
        note.setIsCompleted(noteRequest.getIsCompleted());
        noteRepository.save(note);

        return new NoteResponse(note.getId().intValue(), note.getContent(), note.getIsCompleted());
    }

    @Override
    public void delete(Long id, Integer userId) throws DataNotFoundException {
        Optional<Note> noteOpt = noteRepository.getByIdAndUserId(id, userId);

        if (!noteOpt.isPresent()) {
            throw new NoteNotFoundException();
        }

        Note note = noteOpt.get();
        noteRepository.delete(note);
    }
}
