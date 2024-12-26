package one.june.snippetbox.service;

import one.june.snippetbox.common.Utility;
import one.june.snippetbox.controller.payload.NewUserRequest;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    class CreateUser {
        @Test
        void shouldReturnUserIdForNewUser() {
            try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
                UUID mockedUuid = UUID.fromString("0dcc45b6-7198-401c-85df-10765aac9a57");
                mockedUtility.when(Utility::uuid).thenReturn(mockedUuid);
                when(userRepository.save(any())).thenReturn(User.builder().build());

                UUID userId = userService.createUser(NewUserRequest.builder().name("Some name").build());

                assertEquals(mockedUuid, userId);
                verify(userRepository, Mockito.times(1)).save(any());
            }
        }
    }

    @Nested
    class GetUser {
        @Test
        void shouldReturnUserForGivenId() throws NotFoundException {
            String userId = "0dcc45b6-7198-401c-85df-10765aac9a57";
            when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().name("User name").id(userId).build()));

            User user = userService.getUser(userId);

            assertEquals(User.builder().name("User name").id(userId).build(), user);
        }

        @Test
        void shouldThrowNotFoundExceptionIfUserNotFoundForUserId() {
            String userId = "0dcc45b6-7198-401c-85df-10765aac9a57";
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUser(userId));
            assertEquals("User not found", exception.getMessage());
        }
    }

    @Nested
    class DeleteUser {

        @Test
        void shouldDeleteUserForGivenUserId() {
            doNothing().when(userRepository).deleteById(anyString());

            userService.deleteUser("0dcc45b6-7198-401c-85df-10765aac9a57");

            verify(userRepository, times(1)).deleteById("0dcc45b6-7198-401c-85df-10765aac9a57");
        }

        @Test
        void shouldPropagateExceptionThrownByRepository() {
            doThrow(new IllegalArgumentException()).when(userRepository).deleteById(anyString());

            assertThrows(IllegalArgumentException.class, () -> userService.deleteUser("0dcc45b6-7198-401c-85df-10765aac9a57"));
        }
    }
}