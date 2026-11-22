package repository;

import domain.entities.UserFriendShip;
import domain.exceptions.FormattingException;
import domain.validator.Validator;
import utils.Tuple;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * File repository implementation for UserFriendShip entities.
 */
public class FriendshipFileRepository extends AbstractFileRepository<Tuple<Integer, Integer>, UserFriendShip> {
    public FriendshipFileRepository(String fileName, Validator<Tuple<Integer, Integer>, UserFriendShip> validator) {
        super(fileName, validator);
        super.loadData();
    }

    @Override
    protected UserFriendShip extractEntity(List<String> attributes) {
        if (attributes.size() < 3) throw new FormattingException("Not enough fields for Friendship!");
        try {
            return new UserFriendShip(
                    new Tuple<>(Integer.parseInt(attributes.get(0).trim()), Integer.parseInt(attributes.get(1).trim())),
                    LocalDateTime.parse(attributes.get(2).trim())
            );
        } catch (NumberFormatException e) {
            throw new FormattingException("IDs must be integers!", e);
        } catch (DateTimeParseException e) {
            throw new FormattingException("Invalid date format! Expected ISO-8601 with time.", e);
        }
    }

    @Override
    protected String createEntityAsString(UserFriendShip entity) {
        return entity.toString();
    }
}