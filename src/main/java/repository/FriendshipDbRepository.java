package repository;

import domain.entities.UserFriendShip;
import domain.validator.Validator;
import utils.Tuple;
import java.util.Optional;

/**
 * Skeleton for Friendship database repository
 */
public class FriendshipDbRepository implements Repository<Tuple<Integer, Integer>, UserFriendShip> {
    public FriendshipDbRepository(Validator<Tuple<Integer, Integer>, UserFriendShip> validator, String url, String user, String pass) {}

    @Override
    public Optional<UserFriendShip> add(UserFriendShip entity) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Optional<UserFriendShip> findOne(Tuple<Integer, Integer> id) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Iterable<UserFriendShip> findAll() { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Optional<UserFriendShip> save(UserFriendShip entity) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Optional<UserFriendShip> delete(Tuple<Integer, Integer> id) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Optional<UserFriendShip> update(UserFriendShip entity) { throw new UnsupportedOperationException("Not implemented yet"); }
}