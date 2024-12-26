package one.june.snippetbox.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Exception occurred" + exception.getMessage(), exception);
        List<String> errors = exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ErrorResponse.builder().errorCode("sb-001").reasons(errors).build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        log.error("Exception occurred" + exception.getMessage(), exception);
        return ErrorResponse.builder().errorCode("sb-002").reasons(List.of(exception.getMessage())).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        String errorMessage = e.getMessage();
        log.error("Exception occurred {}", errorMessage, e);
        return ErrorResponse.builder().errorCode("sb-003").reasons(List.of(errorMessage)).build();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        ArrayList<String> errors = new ArrayList<>();
        log.error("Exception occurred {}", ex.getMessage(), ex);

        ex.getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError)
                errors.add("%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()));
            else errors.add(error.getDefaultMessage());
        });

        return ErrorResponse.builder().errorCode("sb-004").reasons(errors).build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException e) {
        String errorMessage = e.getMessage();
        log.error("Exception occurred {}", errorMessage, e);
        return ErrorResponse.builder().errorCode("sb-000").reasons(List.of(e.getMessage())).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Exception occurred {}", errorMessage, e);
        return ErrorResponse.builder().errorCode("sb-000").reasons(List.of(e.getMessage())).build();
    }
}
