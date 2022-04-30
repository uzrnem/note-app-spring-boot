package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entity.Note;

public interface NoteRepository extends CrudRepository<Note, Integer> {

}