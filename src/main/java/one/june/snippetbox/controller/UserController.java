package one.june.snippetbox.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import one.june.snippetbox.controller.payload.NewUserRequest;
import one.june.snippetbox.controller.payload.NewUserResponse;
import one.june.snippetbox.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @PostMapping("")
    public NewUserResponse createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return NewUserResponse.builder().userId(userService.createUser(newUserRequest)).build();
    }
}
