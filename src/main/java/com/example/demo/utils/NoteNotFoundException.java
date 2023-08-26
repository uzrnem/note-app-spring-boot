package com.example.demo.utils;

public class NoteNotFoundException extends DataNotFoundException {
    public NoteNotFoundException() {
        super("Note not found");
    }
}