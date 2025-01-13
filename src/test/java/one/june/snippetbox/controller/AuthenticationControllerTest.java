package one.june.snippetbox.controller;

import one.june.snippetbox.common.JwtUtil;
import one.june.snippetbox.controller.payload.RegisterUserRequest;
import one.june.snippetbox.exception.ExistingUserException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void shouldReturnCreatedUser() throws ExistingUserException {
        when(authenticationService.register(any())).thenReturn(User.builder().build());

        ResponseEntity<User> response = authenticationController.register(new RegisterUserRequest("username", "password"));

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(User.builder().build(), response.getBody());
    }

    @Test
    void shouldPropagateExistingUserExceptionFromService() throws ExistingUserException {
        when(authenticationService.register(any())).thenThrow(new ExistingUserException("User already exists"));

        Assertions.assertThrows(
                ExistingUserException.class,
                () -> authenticationController.register(RegisterUserRequest.builder().build())
        );
    }
}