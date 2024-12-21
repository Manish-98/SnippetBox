package one.june.snippetbox;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    @Autowired
    MongoTemplate mongoTemplate;

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
    }
}
