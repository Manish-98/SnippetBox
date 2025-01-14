package one.june.snippetbox.controller;

import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
    class GetUser {
        @Test
        void shouldReturnUserForGivenId() throws NotFoundException {
            String expectedUserId = "0dcc45b6-7198-401c-85df-10765aac9a57";
            when(userService.getUserById(any())).thenReturn(User.builder().username("User name").id(expectedUserId).build());

            User user = userController.getUser("0dcc45b6-7198-401c-85df-10765aac9a57");

            assertEquals(User.builder().id("0dcc45b6-7198-401c-85df-10765aac9a57").username("User name").build(), user);
        }

        @Test
        void shouldPropagateNotFoundException() throws NotFoundException {
            when(userService.getUserById(any())).thenThrow(new NotFoundException("User not found"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> userController.getUser("0dcc45b6-7198-401c-85df-10765aac9a57"));
            assertEquals("User not found", exception.getMessage());
        }
    }

    @Nested
    class DeleteUser {
        @Test
        void shouldDeleteUserWithGivenId() {
            doNothing().when(userService).deleteUser(anyString());

            userController.deleteUser("0dcc45b6-7198-401c-85df-10765aac9a57");

            verify(userService, times(1)).deleteUser("0dcc45b6-7198-401c-85df-10765aac9a57");
        }

        @Test
        void shouldPropagateExceptionThrownByService() {
            doThrow(new IllegalArgumentException()).when(userService).deleteUser(anyString());

            assertThrows(IllegalArgumentException.class, () -> userController.deleteUser("0dcc45b6-7198-401c-85df-10765aac9a57"));
        }
    }
}