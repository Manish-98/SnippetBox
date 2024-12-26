package one.june.snippetbox.controller;

import one.june.snippetbox.controller.payload.NewSnippetRequest;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.Snippet;
import one.june.snippetbox.service.SnippetService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SnippetControllerTest {

    @Mock
    private SnippetService snippetService;

    @InjectMocks
    private SnippetController snippetController;

    @Nested
    class CreateSnippet {
        @Test
        void shouldReturnSnippetWhenCreationSuccessful() throws NotFoundException {
            when(snippetService.newSnippet(any())).thenReturn(Snippet.builder().id("088907f1-dd98-4c46-a701-d1a0c70ac5d5").build());

            Snippet snippet = snippetController.createSnippet(NewSnippetRequest.builder().title("Test").userId("088907f1-dd98-4c46-a701-d1a0c70ac5d4").snippet("Test Snippet").build());

            assertEquals(Snippet.builder().id("088907f1-dd98-4c46-a701-d1a0c70ac5d5").build(), snippet);
        }

        @Test
        void shouldPropagateExceptionThrownByService() throws NotFoundException {
            doThrow(new NotFoundException("User not found")).when(snippetService).newSnippet(any());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> snippetController.createSnippet(NewSnippetRequest.builder().build()));
            assertEquals("User not found", exception.getMessage());
        }
    }

}