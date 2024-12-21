package one.june.snippetbox.exception;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private List<String> reasons;
}
