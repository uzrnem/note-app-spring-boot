package com.example.demo.utils;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@ResponseBody
public class ErrorHandler {
    @ExceptionHandler(value = {NoteNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<HashMap<String, String>> resourceNotFoundException(DataNotFoundException ex, WebRequest request) {
        HashMap<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return new ResponseEntity<HashMap<String, String>>(errorMap, HttpStatus.NOT_FOUND);
    }
}