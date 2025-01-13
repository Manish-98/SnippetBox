
package one.june.snippetbox;

import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.SnippetRepository;
import one.june.snippetbox.repository.UserRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SnippetControllerIntegrationTest {
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

    @Autowired
    private SnippetRepository snippetRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(User.builder().id("0dcc45b6-7198-401c-85df-10765aac9a57").username("Test user").build());
        snippetRepository.deleteAll();
    }

    @Nested
    class NewSnippet {
        @Test
        void shouldCreateNewSnippet() throws JSONException {
            String snippetRequest = """
                    {
                        "title": "Snippet",
                        "userId": "0dcc45b6-7198-401c-85df-10765aac9a57",
                        "snippet": "Some code snippet"
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(snippetRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange("/snippets", HttpMethod.POST, request, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            String expectedResponse = """
                    {"title":"Snippet","user":{"id":"0dcc45b6-7198-401c-85df-10765aac9a57","username":"Test user"},"snippet":"Some code snippet"}""";
            JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
        }

        @Test
        void shouldReturnNotFoundWhenUserNotFound() throws JSONException {
            String snippetRequest = """
                    {
                        "title": "Snippet",
                        "userId": "0dcc45b6-7198-401c-85df-10765aac9a56",
                        "snippet": "Some code snippet"
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(snippetRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange("/snippets", HttpMethod.POST, request, String.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            String expectedResponse = """
                    {"errorCode":"sb-002","reasons":["User not found"]}""";
            JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
        }

        @Test
        void shouldReturnBadRequestWhenRequestIsInvalid() throws JSONException {
            String snippetRequest = """
                    {
                        "title": "Snippet",
                        "userId": "0dcc45b6-7198-401c-85df",
                        "snippet": "Some code snippet"
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(snippetRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange("/snippets", HttpMethod.POST, request, String.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            String expectedResponse = """
                    {"errorCode":"sb-001","reasons":["Invalid user id"]}""";
            System.out.println(response.getBody());
            JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
        }
    }
}
