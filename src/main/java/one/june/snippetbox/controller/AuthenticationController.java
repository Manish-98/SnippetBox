package one.june.snippetbox.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import one.june.snippetbox.common.JwtUtil;
import one.june.snippetbox.controller.payload.RegisterUserRequest;
import one.june.snippetbox.exception.ExistingUserException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserRequest registerUserRequest) throws ExistingUserException {
        User user = authenticationService.register(registerUserRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
