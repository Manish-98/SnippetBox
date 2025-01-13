package one.june.snippetbox.service;

import one.june.snippetbox.common.Utility;
import one.june.snippetbox.controller.payload.RegisterUserRequest;
import one.june.snippetbox.exception.ExistingUserException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void shouldReturnUserAfterCreation() throws ExistingUserException {
        try(MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
            mockedUtility.when(Utility::uuid).thenReturn(UUID.fromString("0dcc45b6-7198-401c-85df-10765aac9a57"));
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            when(userRepository.save(any())).thenAnswer(request -> request.getArgument(0));

            User user = authenticationService.register(new RegisterUserRequest("username", "password"));

            User newUser = User.builder()
                    .id("0dcc45b6-7198-401c-85df-10765aac9a57")
                    .username("username")
                    .password("encodedPassword")
                    .build();
            Assertions.assertEquals(newUser, user);
        }
    }

    @Test
    void shouldThrowExceptionWhenUserExists() {
        try(MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
            mockedUtility.when(Utility::uuid).thenReturn(UUID.fromString("0dcc45b6-7198-401c-85df-10765aac9a57"));
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(User.builder().build()));

            Assertions.assertThrows(
                    ExistingUserException.class,
                    () -> authenticationService.register(new RegisterUserRequest("username", "password"))
            );

            verify(userRepository, times(1)).findByUsername("username");
            verify(userRepository, times(0)).save(any());
        }
    }
}