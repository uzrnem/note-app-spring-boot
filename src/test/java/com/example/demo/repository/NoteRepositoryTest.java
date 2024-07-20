package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.Note;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class NoteRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    public void NoteRepository_SaveAll_ReturnSavedNote() {

        User user = new User("uzrnem@gmail.com", "gisue");
        User savedUser = userRepository.save(user);

        Note note = new Note("Sample Note", user, true);
        Note savedNote = noteRepository.save(note);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);

        
        Assertions.assertThat(savedNote).isNotNull();
        Assertions.assertThat(savedNote.getId()).isGreaterThan(0);
    }

    @Test
    public void NoteRepository_FindById_ReturnNote() {
        User user = new User("uzrnem@gmail.com", "gisue");
        userRepository.save(user);

        Note note = new Note("Sample Note", user, true);
        noteRepository.save(note);

        User userList = userRepository.findById(user.getId()).get();
        Assertions.assertThat(userList).isNotNull();

        Note noteList = noteRepository.findById(note.getId()).get();
        Assertions.assertThat(noteList).isNotNull();
    }

    @Test
    public void NoteRepository_NoteDelete_ReturnNoteIsEmpty() {
        User user = new User("uzrnem@gmail.com", "gisue");
        userRepository.save(user);

        Note note = new Note("Sample Note", user, true);
        noteRepository.save(note);

        noteRepository.deleteById(note.getId());
        Optional<Note> noteReturn = noteRepository.findById(note.getId());

        Assertions.assertThat(noteReturn).isEmpty();

        userRepository.deleteById(user.getId());
        Optional<User> userReturn = userRepository.findById(user.getId());

        Assertions.assertThat(userReturn).isEmpty();
    }
}