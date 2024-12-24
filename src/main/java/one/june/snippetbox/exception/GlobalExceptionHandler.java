package one.june.snippetbox.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Exception occurred" + exception.getMessage());
        List<String> errors = exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ErrorResponse.builder().errorCode("user-001").reasons(errors).build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        log.error("Exception occurred" + exception.getMessage());
        return ErrorResponse.builder().errorCode("user-002").reasons(List.of(exception.getMessage())).build();
    }
}
