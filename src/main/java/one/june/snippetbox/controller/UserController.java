package one.june.snippetbox.controller;

import lombok.AllArgsConstructor;
import one.june.snippetbox.common.UUID;
import one.june.snippetbox.exception.NotFoundException;
import one.june.snippetbox.model.User;
import one.june.snippetbox.service.UserService;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @GetMapping("/{userId}")
    public User getUser(@PathVariable @UUID(message = "Invalid user id") String userId) throws NotFoundException {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @UUID(message = "Invalid user id") String userId) {
        userService.deleteUser(userId);
    }
}
