package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    User user;

    @BeforeEach
    void setUp() {
        user = new User("uzrnem@gmail.com", "gisue");
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        user = null;
        userRepository.deleteAll();
    }

    @Test
    void testExistsByEmail()
    {
        Boolean isUserPresent = userRepository.existsByEmail("uzrnem@gmail.com");
        assertThat(isUserPresent).isEqualTo(true);
    }

    @Test
    void testFindByVendorName_Found()
    {
        Optional<User> user = userRepository.findByEmail("uzrnem@gmail.com");
        assertThat(user.isPresent()).isEqualTo(true);
    }
}