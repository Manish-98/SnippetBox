package one.june.snippetbox;

import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    private static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:6.0.4");

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void configureMongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    class GetUser {
        @Test
        void shouldGetUserForGivenId() {
            userRepository.save(
                    User.builder()
                            .id("0dcc45b6-7198-401c-85df-10765aac9a57")
                            .username("Some user")
                            .build()
            );

            ResponseEntity<User> response = restTemplate.getForEntity("/users/0dcc45b6-7198-401c-85df-10765aac9a57", User.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(
                    User.builder()
                            .id("0dcc45b6-7198-401c-85df-10765aac9a57")
                            .username("Some user")
                            .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                            .updatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                            .build(),
                    response.getBody()
            );
        }

        @Test
        void shouldReturnNotFoundIfUserIdIsInvalid() {
            ResponseEntity<Void> response = restTemplate.getForEntity("/users/some-id", Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class DeleteUser {
        @Test
        void shouldDeleteUserWithGivenUserId() {
            userRepository.save(User.builder().id("0dcc45b6-7198-401c-85df-10765aac9a57").username("Some name").build());

            ResponseEntity<Void> response = restTemplate.exchange("/users/0dcc45b6-7198-401c-85df-10765aac9a57", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, Map.of());

            assertEquals(HttpStatus.OK, response.getStatusCode());
            Optional<User> user = userRepository.findById("0dcc45b6-7198-401c-85df-10765aac9a57");
            assertTrue(user.isEmpty());
        }

        @Test
        void shouldReturnNotFoundIfUserIdIsInvalid() {
            ResponseEntity<String> response = restTemplate.exchange("/users/some-id", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
