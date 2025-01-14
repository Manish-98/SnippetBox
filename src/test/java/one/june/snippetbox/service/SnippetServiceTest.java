package one.june.snippetbox.service;


import one.june.snippetbox.common.Utility;
import one.june.snippetbox.controller.payload.NewSnippetRequest;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.Snippet;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.SnippetRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SnippetServiceTest {

    @Mock
    private SnippetRepository snippetRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SnippetService snippetService;

    @Nested
    class CreateSnippet {
        @Test
        void shouldReturnSnippetWhenCreationSuccessful() throws NotFoundException {
            try(MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
                mockedUtility.when(Utility::uuid).thenReturn(UUID.fromString("0dcc45b6-7198-401c-85df-10765aac9a57"));
                User user = User.builder().username("Test user").id("088907f1-dd98-4c46-a701-d1a0c70ac5d4").build();
                when(userService.getUserByUsername(anyString())).thenReturn(user);
                when(snippetRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

                Snippet snippet = snippetService.newSnippet(
                        NewSnippetRequest.builder().title("Snippet").build(),
                        User.builder().username("Test user").password("password").build()
                );

                assertEquals(Snippet.builder().id("0dcc45b6-7198-401c-85df-10765aac9a57").user(user).title("Snippet").build(), snippet);
            }
        }

        @Test
        void shouldThrowNotFoundExceptionIfUserNotFound() throws NotFoundException {
            doThrow(new NotFoundException("User not found")).when(userService).getUserByUsername(any());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> snippetService.newSnippet(
                            NewSnippetRequest.builder().build(),
                            User.builder().username("Test user").password("password").build()
                    )
            );

            assertEquals("User not found", exception.getMessage());
        }
    }
}