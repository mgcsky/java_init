package com.example.demo.user.domain;

import com.example.demo.user.infrastructure.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;

@DataJpaTest
public class UserEntityTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void prePersist_shouldSetCreatedAt_whenSaved() {
        User user = User.builder()
                .username("Albion")
                .email("albion@test.com")
                .phone("+1234566342")
                .passwordHash("testing_hash_string")
                .build();

        Assertions.assertNull(user.getCreatedAt());

        entityManager.persistAndFlush(user);

        Assertions.assertNotNull(user.getCreatedAt());
        Assertions.assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void prePersist_shouldSetStatus_whenSaved() {
        User user = User.builder()
                .username("Albion")
                .email("albion@test.com")
                .phone("+1234566342")
                .passwordHash("testing_hash_string")
                .build();

        Assertions.assertNull(user.getStatus());

        entityManager.persistAndFlush(user);

        Assertions.assertNotNull(user.getStatus());
        Assertions.assertEquals(UserStatus.INITIATED, user.getStatus());
    }

}
