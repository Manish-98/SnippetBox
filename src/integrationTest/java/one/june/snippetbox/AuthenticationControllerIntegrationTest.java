package one.june.snippetbox;

import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerIntegrationTest {
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
        userRepository.save(User.builder().id("0dcc45b6-7198-401c-85df-10765aac9a57").username("Test user").build());
    }

    @Nested
    class RegisterUser {
        @Test
        void shouldRegisterNewUser() throws JSONException {
            String userRequest = """
                    {
                        "username": "Some name",
                        "password": "some password"
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange("/auth/register", HttpMethod.POST, request, String.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            JSONAssert.assertEquals("""
                    {
                      "username": "Some name",
                      "roles":["USER"],
                      "enabled": true,
                      "accountNonExpired": true,
                      "accountNonLocked": true,
                      "credentialsNonExpired": true
                    }""", response.getBody(), false);
        }

        @Test
        void shouldReturnErrorMessageWhenUserRegistrationRequestIsInvalid() throws JSONException {
            String userRequest = """
                    {
                        "username": "Some name"
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange("/auth/register", HttpMethod.POST, request, String.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            JSONAssert.assertEquals(
                    "{\"errorCode\": \"sb-001\",\"reasons\": [\"Password must not be blank\"]}", response.getBody(), true
            );
        }

        @Test
        void shouldReturnErrorMessageIfGivenUserAlreadyExists() throws JSONException {
            String userRequest = """
                    {
                        "username": "Test user",
                        "password": "some password"
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange("/auth/register", HttpMethod.POST, request, String.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            JSONAssert.assertEquals(
                    "{\"errorCode\": \"sb-005\",\"reasons\": [\"User already exists\"]}", response.getBody(), true
            );
        }
    }
}
