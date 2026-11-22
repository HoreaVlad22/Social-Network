package domain.validator;

import domain.entities.User;
import domain.exceptions.ValidatorException;

/**
 * Validator implementation for User entities.
 */
public class UserValidator implements Validator<Integer, User> {
    @Override
    public void validate(User user) throws ValidatorException {
        StringBuilder errors = new StringBuilder();
        if (user.getName().isEmpty() || user.getSurname().isEmpty()) errors.append("Name/surname cannot be empty.\n");
        if (errors.length() > 0) throw new ValidatorException(errors.toString());
    }
}