package domain.exceptions;

/**
 * Exception thrown for general repository errors, including IO operations.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message)
    { super(message); }
    public RepositoryException(String message, Throwable cause)
    { super(message, cause); }
}