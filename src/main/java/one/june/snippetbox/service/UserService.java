package one.june.snippetbox.service;

import lombok.AllArgsConstructor;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User getUserById(String userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getUserByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
