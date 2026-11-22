package repository;

import domain.entities.User;
import domain.validator.Validator;
import java.util.Optional;

/**
 * Skeleton for database repository
 */
public class UserDbRepository implements Repository<Integer, User> {
    public UserDbRepository(Validator<Integer, User> validator, String url, String user, String pass) {}

    @Override
    public Optional<User> add(User entity) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Optional<User> findOne(Integer id) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Iterable<User> findAll() { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Optional<User> save(User entity) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Optional<User> delete(Integer id) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override
    public Optional<User> update(User entity) { throw new UnsupportedOperationException("Not implemented yet"); }
}