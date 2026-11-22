package domain.exceptions;

/**
 * Exception thrown when an entity fails the validation process.
 */
public class ValidatorException extends RuntimeException {
    public ValidatorException(String message)
    { super(message); }
}
