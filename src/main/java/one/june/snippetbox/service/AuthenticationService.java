package one.june.snippetbox.service;

import lombok.AllArgsConstructor;
import one.june.snippetbox.common.Utility;
import one.june.snippetbox.controller.payload.RegisterUserRequest;
import one.june.snippetbox.exception.ExistingUserException;
import one.june.snippetbox.model.Role;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterUserRequest registerUserRequest) throws ExistingUserException {
        UUID userId = Utility.uuid();
        Optional<User> existingUser = userRepository.findByUsername(registerUserRequest.getUsername());
        if (existingUser.isPresent()) throw new ExistingUserException("User already exists");

        User newUser = User.builder()
                .id(userId.toString())
                .username(registerUserRequest.getUsername())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .roles(List.of(Role.USER))
                .build();
        return userRepository.save(newUser);
    }
}
