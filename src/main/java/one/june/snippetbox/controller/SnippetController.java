package one.june.snippetbox.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import one.june.snippetbox.controller.payload.NewSnippetRequest;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.exception.UnauthorisedUserException;
import one.june.snippetbox.model.Snippet;
import one.june.snippetbox.model.User;
import one.june.snippetbox.service.SnippetService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/snippets")
public class SnippetController {
    private SnippetService snippetService;

    @PostMapping("")
    public Snippet createSnippet(@RequestBody @Valid NewSnippetRequest newSnippetRequest) throws NotFoundException, UnauthorisedUserException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return snippetService.newSnippet(newSnippetRequest, user);
    }
}
