package com.example.demo.services;

import java.util.List;
import com.example.demo.schema.NoteRequest;
import com.example.demo.schema.NoteResponse;
import com.example.demo.utils.DataNotFoundException;
import com.example.demo.utils.NoteNotFoundException;

public interface NoteService {
    
    public List<NoteResponse> list(Integer userId);
    public NoteResponse add(NoteRequest noteRequest, Integer userId) throws DataNotFoundException;
    public NoteResponse get(Long id, Integer userId) throws NoteNotFoundException;
    public NoteResponse update(Long id, NoteRequest noteRequest, Integer userId) throws DataNotFoundException;
    public void delete(Long id, Integer userId) throws DataNotFoundException;
}