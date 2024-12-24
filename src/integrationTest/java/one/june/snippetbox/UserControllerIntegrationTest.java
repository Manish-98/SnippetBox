package one.june.snippetbox;

import one.june.snippetbox.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    private static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:6.0.4");

    static {
        mongoDBContainer.start(); // Start the container before all tests
    }

    @DynamicPropertySource
    static void configureMongoProperties(DynamicPropertyRegistry registry) {
        // Override Spring Boot's MongoDB properties with Testcontainers' connection details
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    TestRestTemplate restTemplate;


    @Nested
    class NewUser {
        @Test
        void shouldCreateNewUser() {
            String userRequest = """
                    {
                        "name": "Some name"
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, request, String.class);

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturnBadRequestWhenUserNameIsInvalid() {
            String userRequest = """
                    {
                        "name": "Invalid Name!"
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(userRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, request, String.class);

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            Assertions.assertEquals("{\"errorCode\":\"user-001\",\"reasons\":[\"Username must contain only alphanumeric characters or underscore (A-Z, a-z, 0-9, _)\"]}", response.getBody());
        }
    }
}
