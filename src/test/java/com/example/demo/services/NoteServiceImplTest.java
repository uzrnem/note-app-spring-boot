package com.example.demo.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.schema.NoteResponse;
import com.example.demo.services.impl.NoteServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    Note note;
    @BeforeEach
    void setUp() {
    	note = new Note(1,"Bhagyesh", new User(1, "email", "pass"), true);
    }

    @AfterEach
    void tearDown() throws Exception {}

    @Test
    void testGetAll() {
        when(noteRepository.getByUserId(anyInt())).thenReturn(List.of(note));

        List<NoteResponse> list = noteService.list(1);
        
        assertEquals(list.get(0).getContent(), note.getContent());
        ArgumentCaptor<Integer> intArgument = ArgumentCaptor.forClass(Integer.class);
        verify(noteRepository).getByUserId(intArgument.capture());
        assertEquals(note.getUser().getId(), intArgument.getValue());
    }
    
    //Future scope logger capture and argument captor
    /*
   
   verify(mock, times(2)).doSomething(peopleCaptor.capture());
   List<Person> capturedPeople = peopleCaptor.getAllValues();
     */
}