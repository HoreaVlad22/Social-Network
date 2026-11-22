package repository;

import domain.entities.User;
import domain.exceptions.FormattingException;
import domain.validator.Validator;
import java.util.List;

/**
 * File repository implementation for User entities.
 */
public class UserFileRepository extends AbstractFileRepository<Integer, User> {
    public UserFileRepository(String fileName, Validator<Integer, User> validator) {
        super(fileName, validator);
        super.loadData();
    }

    @Override
    protected User extractEntity(List<String> attributes) {
        if (attributes.size() < 5) throw new FormattingException("Not enough fields for User!");
        try {
            return new User(
                    Integer.parseInt(attributes.get(0).trim()),
                    attributes.get(1).trim(),
                    attributes.get(2).trim(),
                    attributes.get(3).trim(),
                    attributes.get(4).trim()
            );
        } catch (NumberFormatException e) {
            throw new FormattingException("User ID must be an integer!", e);
        }
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getName() + ";" + entity.getSurname() + ";" + entity.getUsername() + ";" + entity.getPassword();
    }
}