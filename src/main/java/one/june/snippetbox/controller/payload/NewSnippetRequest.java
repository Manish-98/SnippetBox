package one.june.snippetbox.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.june.snippetbox.common.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewSnippetRequest {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_ ]+", message = "Snippet title must contain only alphanumeric characters or underscore (A-Z, a-z, 0-9, _)")
    private String title;

    @UUID(message = "Invalid user id")
    private String userId;

    @NotBlank(message = "Snippet must not be empty")
    private String snippet;
}
