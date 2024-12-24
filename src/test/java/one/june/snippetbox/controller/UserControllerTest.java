package one.june.snippetbox.controller;

import one.june.snippetbox.controller.payload.NewUserRequest;
import one.june.snippetbox.controller.payload.NewUserResponse;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Nested
    class CreateUser {
        @Test
        void shouldReturnUserIdOfCreatedUser() {
            UUID expectedUserId = UUID.fromString("0dcc45b6-7198-401c-85df-10765aac9a57");
            when(userService.createUser(any())).thenReturn(expectedUserId);

            NewUserResponse newUserResponse = userController.createUser(NewUserRequest.builder().name("Some name").build());

            assertEquals(expectedUserId, newUserResponse.getUserId());
            verify(userService, times(1)).createUser(NewUserRequest.builder().name("Some name").build());
        }
    }

    @Nested
    class GetUser {
        @Test
        void shouldReturnUserForGivenId() throws NotFoundException {
            String expectedUserId = "0dcc45b6-7198-401c-85df-10765aac9a57";
            when(userService.getUser(anyString())).thenReturn(User.builder().name("User name").id(expectedUserId).build());

            User user = userController.getUser("0dcc45b6-7198-401c-85df-10765aac9a57");

            assertEquals(User.builder().id("0dcc45b6-7198-401c-85df-10765aac9a57").name("User name").build(), user);
        }

        @Test
        void shouldPropagateNotFoundException() throws NotFoundException {
            when(userService.getUser(anyString())).thenThrow(new NotFoundException("User not found"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> userController.getUser("0dcc45b6-7198-401c-85df-10765aac9a57"));
            assertEquals("User not found", exception.getMessage());
        }
    }
}