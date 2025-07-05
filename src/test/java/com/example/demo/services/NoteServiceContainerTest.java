package com.example.demo.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.schema.NoteResponse;
import com.example.demo.services.impl.NoteServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class NoteServiceContainerTest {

    static final MySQLContainer MY_SQL_CONTAINER;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("docker.io/library/mysql:oraclelinux9");
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",() -> MY_SQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username",() -> MY_SQL_CONTAINER.getUsername());
        registry.add("spring.datasource.password",() -> MY_SQL_CONTAINER.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto",() -> "create");

    }

    @BeforeEach
    public void beforeEach(){

        User user = new User("uzrnem@gmail.com", "bhagyesh123");
        userRepository.save(user);

        Note note = new Note("noteRequest.getContent()", user, false);
        noteRepository.save(note);
    }
    @AfterEach
    public void afterEach(){
        noteRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetAll() {

        List<NoteResponse> list = noteService.list(1);
        assertNotNull(list);
        System.out.println("List size: " + list.size());
        list.forEach(noteResponse -> {
            System.out.println("Note Content: " + noteResponse.getContent());
        });
    }
}