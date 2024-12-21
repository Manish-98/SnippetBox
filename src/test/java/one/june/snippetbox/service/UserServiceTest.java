package one.june.snippetbox.service;

import one.june.snippetbox.common.Utility;
import one.june.snippetbox.controller.payload.NewUserRequest;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
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

                Assertions.assertEquals(mockedUuid, userId);
                verify(userRepository, Mockito.times(1)).save(any());
            }
        }
    }

}