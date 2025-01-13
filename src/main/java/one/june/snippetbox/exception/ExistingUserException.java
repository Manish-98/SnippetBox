package one.june.snippetbox.exception;

public class ExistingUserException extends Exception {
    public ExistingUserException(String message) {
        super(message);
    }
}
