package one.june.snippetbox.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "Username must not be blank")
    @Pattern(regexp = "[a-zA-Z0-9_ ]+", message = "Username must contain only alphanumeric characters or underscore (A-Z, a-z, 0-9, _)")
    private String name;
}
