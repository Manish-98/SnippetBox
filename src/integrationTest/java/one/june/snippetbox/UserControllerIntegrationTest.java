package one.june.snippetbox;

import one.june.snippetbox.common.JwtUtil;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.time.LocalDateTime;
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
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(User.builder().id("0dcc45b6-7198-401c-85df-10765aac9a57").username("Some user").build());
    }

    @Nested
    class GetUser {
        @Test
        void shouldGetUserForGivenId() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTb21lIHVzZXIiLCJpYXQiOjE3MzY3NjMwMjMsImV4cCI6MjA1MjEyMzAyM30.GZso9zcIEy4csW0H0fPYBONy62wiTT4WCL6RzVTLHQs");
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<User> response = restTemplate.exchange("/users/0dcc45b6-7198-401c-85df-10765aac9a57", HttpMethod.GET, requestEntity, User.class);

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
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTb21lIHVzZXIiLCJpYXQiOjE3MzY3NjMwMjMsImV4cCI6MjA1MjEyMzAyM30.GZso9zcIEy4csW0H0fPYBONy62wiTT4WCL6RzVTLHQs");
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Void> response = restTemplate.exchange("/users/some-id", HttpMethod.GET, requestEntity, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class DeleteUser {
        @Test
        void shouldDeleteUserWithGivenUserId() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTb21lIHVzZXIiLCJpYXQiOjE3MzY3NjMwMjMsImV4cCI6MjA1MjEyMzAyM30.GZso9zcIEy4csW0H0fPYBONy62wiTT4WCL6RzVTLHQs");
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Void> response = restTemplate.exchange("/users/0dcc45b6-7198-401c-85df-10765aac9a57", HttpMethod.DELETE, requestEntity, Void.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            Optional<User> user = userRepository.findById("0dcc45b6-7198-401c-85df-10765aac9a57");
            assertTrue(user.isEmpty());
        }

        @Test
        void shouldReturnNotFoundIfUserIdIsInvalid() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTb21lIHVzZXIiLCJpYXQiOjE3MzY3NjMwMjMsImV4cCI6MjA1MjEyMzAyM30.GZso9zcIEy4csW0H0fPYBONy62wiTT4WCL6RzVTLHQs");
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange("/users/some-id", HttpMethod.DELETE, requestEntity, String.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
