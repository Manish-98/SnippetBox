package one.june.snippetbox.service;

import lombok.AllArgsConstructor;
import one.june.snippetbox.common.Utility;
import one.june.snippetbox.controller.payload.NewUserRequest;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public UUID createUser(NewUserRequest newUserRequest) {
        UUID userId = Utility.uuid();
        User newUser = User.builder().id(userId.toString()).name(newUserRequest.getName()).build();
        userRepository.save(newUser);
        return userId;
    }

    public User getUser(String userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
