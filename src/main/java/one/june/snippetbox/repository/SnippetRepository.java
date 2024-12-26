package one.june.snippetbox.repository;

import one.june.snippetbox.model.Snippet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnippetRepository extends MongoRepository<Snippet, String> {
}
