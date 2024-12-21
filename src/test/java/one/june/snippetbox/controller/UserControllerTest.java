package one.june.snippetbox.controller;

import one.june.snippetbox.controller.payload.NewUserRequest;
import one.june.snippetbox.controller.payload.NewUserResponse;
import one.june.snippetbox.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
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

            Assertions.assertEquals(expectedUserId, newUserResponse.getUserId());
            verify(userService, times(1)).createUser(NewUserRequest.builder().name("Some name").build());
        }
    }
}