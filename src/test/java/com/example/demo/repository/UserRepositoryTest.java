package com.example.demo.repository;

import com.example.demo.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnSavedUser() {

        User user = new User("uzrnem@gmail.com", "gisue");
        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_FindById_ReturnUser() {
        User user = new User("uzrnem@gmail.com", "gisue");

        userRepository.save(user);

        User userList = userRepository.findById(user.getId()).get();

        Assertions.assertThat(userList).isNotNull();
    }

    @Test
    public void UserRepository_UserDelete_ReturnUserIsEmpty() {
        User user = new User("uzrnem@gmail.com", "gisue");

        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> userReturn = userRepository.findById(user.getId());

        Assertions.assertThat(userReturn).isEmpty();
    }
/*
    @Test
    public void testFindByEmail() {
        User user = new User("uzrnem@gmail.com", "gisue");

        userRepository.save(user);
        
        //User user2 = userRepository.findFirstByEmail(user.getEmail());

        Optional<User> userReturn = userRepository.findByEmail(user.getEmail());

        Assertions.assertThat(userReturn).isNotEmpty();
    } */
}