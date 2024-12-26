package one.june.snippetbox.service;

import lombok.AllArgsConstructor;
import one.june.snippetbox.common.Utility;
import one.june.snippetbox.controller.payload.NewSnippetRequest;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.Snippet;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.SnippetRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SnippetService {
    private UserService userService;
    private SnippetRepository snippetRepository;

    public Snippet newSnippet(NewSnippetRequest newSnippetRequest) throws NotFoundException {
        String userId = newSnippetRequest.getUserId();
        User user = userService.getUser(userId);

        UUID snippetId = Utility.uuid();

        Snippet snippet = Snippet.builder()
                .id(snippetId.toString())
                .title(newSnippetRequest.getTitle())
                .snippet(newSnippetRequest.getSnippet())
                .user(user)
                .build();

        return snippetRepository.save(snippet);
    }
}
