package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> getByUserId(Integer userId);

    Optional<Note> getByIdAndUserId( Long id, Integer userId);
}
