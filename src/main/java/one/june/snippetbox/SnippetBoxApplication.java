package one.june.snippetbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class SnippetBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnippetBoxApplication.class, args);
    }

}
