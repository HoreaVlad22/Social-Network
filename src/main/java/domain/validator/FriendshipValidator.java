package domain.validator;

import domain.entities.UserFriendShip;
import domain.exceptions.ValidatorException;
import utils.Tuple;
import java.time.LocalDateTime;

/**
 * Validator implementation for UserFriendShip entities.
 */
public class FriendshipValidator implements Validator<Tuple<Integer, Integer>, UserFriendShip> {
    @Override
    public void validate(UserFriendShip friendship) throws ValidatorException {
        StringBuilder errors = new StringBuilder();
        if (friendship.getId().getE1() < 0 || friendship.getId().getE2() < 0) {
            errors.append("IDs cannot be negative.\n");
        }
        if (friendship.getFriendshipDay().isAfter(LocalDateTime.now())) {
            errors.append("Friendship date cannot be in the future.\n");
        }
        if (errors.length() > 0) throw new ValidatorException(errors.toString());
    }
}