package one.june.snippetbox.exception;

public class UnauthorisedUserException extends Exception {
    public UnauthorisedUserException(String message) {
        super(message);
    }
}
