package domain.exceptions;

/**
 * Exception thrown when file data cannot be parsed correctly.
 */
public class FormattingException extends RuntimeException {
    public FormattingException(String message)
    { super(message); }
    public FormattingException(String message, Throwable cause) { super(message, cause); }
}