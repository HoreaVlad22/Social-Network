package domain.entities;

import utils.Tuple;
import java.time.LocalDateTime;

/**
 * Represents a friendship relationship between two users.
 */
public class UserFriendShip extends Entity<Tuple<Integer, Integer>> {
    private final LocalDateTime friendshipDay;

    public UserFriendShip(Tuple<Integer, Integer> id, LocalDateTime friendshipDay) {
        super(id);
        this.friendshipDay = friendshipDay;
    }

    public LocalDateTime getFriendshipDay() { return friendshipDay; }

    @Override
    public String toString() {
        return id.getE1() + ";" + id.getE2() + ";" + friendshipDay;
    }
}