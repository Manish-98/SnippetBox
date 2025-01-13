package one.june.snippetbox.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterUserRequest {
    @NotBlank(message = "Username must not be blank")
    @NotNull(message = "Username missing")
    @Pattern(regexp = "[a-zA-Z0-9_ ]+", message = "Username must contain only alphanumeric characters or underscore (A-Z, a-z, 0-9, _)")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Pattern(regexp = ".{8,}", message = "Password must be at least 8 characters long")
    private String password;
}
