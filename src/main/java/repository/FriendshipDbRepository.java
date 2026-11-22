package repository;

import domain.entities.UserFriendShip;
import domain.exceptions.RepositoryException;
import domain.validator.Validator;
import utils.Tuple;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL JDBC implementation of {@link Repository} for {@link UserFriendShip} entities.
 */
public class FriendshipDbRepository implements Repository<Tuple<Integer, Integer>, UserFriendShip> {
    private final Validator<Tuple<Integer, Integer>, UserFriendShip> validator;
    private final String url;
    private final String username;
    private final String password;

    /**
     * Creates a database repository for friendships.
     * @param validator the validator used before saving or updating friendships
     * @param url the JDBC connection URL
     * @param username the database username
     * @param password the database password
     */
    public FriendshipDbRepository(Validator<Tuple<Integer, Integer>, UserFriendShip> validator,
                                  String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private RepositoryException translateSqlException(SQLException e, String context) {
        String state = e.getSQLState();
        if (state != null && state.startsWith("08")) {
            return new RepositoryException("Conexiunea la baza de date a esuat (serverul este oprit sau inaccesibil).", e);
        }
        if ("23505".equals(state)) {
            return new RepositoryException("Prietenia exista deja in baza de date!", e);
        }
        if ("23503".equals(state)) {
            return new RepositoryException("Unul sau ambii utilizatori nu exista in baza de date!", e);
        }
        if ("23514".equals(state)) {
            return new RepositoryException("Constrangere incalcata: id1 trebuie sa fie mai mic decat id2!", e);
        }
        return new RepositoryException("Eroare baza de date (" + context + "): " + e.getMessage(), e);
    }

    private UserFriendShip extractFriendship(ResultSet rs) throws SQLException {
        int id1 = rs.getInt("id1");
        int id2 = rs.getInt("id2");
        LocalDateTime friendsFrom = rs.getTimestamp("friends_from").toLocalDateTime();
        return new UserFriendShip(new Tuple<>(id1, id2), friendsFrom);
    }

    /**
     * Adds a new friendship to the database.
     * @param entity the friendship to add
     * @return {@code Optional.empty()} if added, or {@code Optional.of(entity)} if it already exists
     * @throws IllegalArgumentException if {@code entity} is null
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Optional<UserFriendShip> add(UserFriendShip entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);

        if (findOne(entity.getId()).isPresent()) {
            return Optional.of(entity);
        }

        String sql = "INSERT INTO friendships (id1, id2, friends_from) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entity.getId().getE1());
            ps.setInt(2, entity.getId().getE2());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getFriendshipDay()));
            ps.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw translateSqlException(e, "add friendship");
        }
    }

    /**
     * Saves a friendship (delegates to {@link #add(UserFriendShip)}).
     * @param entity the friendship to save
     * @return {@code Optional.empty()} if saved, or {@code Optional.of(entity)} if it already exists
     * @throws IllegalArgumentException if {@code entity} is null
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Optional<UserFriendShip> save(UserFriendShip entity) {
        return add(entity);
    }

    /**
     * Finds a friendship by its composite key {@code (id1, id2)}.
     * @param id the pair of user IDs
     * @return an {@code Optional} containing the friendship if found, or empty otherwise
     * @throws IllegalArgumentException if {@code id} is null
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Optional<UserFriendShip> findOne(Tuple<Integer, Integer> id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        String sql = "SELECT id1, id2, friends_from FROM friendships WHERE id1 = ? AND id2 = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id.getE1());
            ps.setInt(2, id.getE2());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractFriendship(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw translateSqlException(e, "findOne friendship");
        }
    }

    /**
     * Retrieves all friendships from the database.
     * @return an {@code Iterable} of all friendships
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Iterable<UserFriendShip> findAll() {
        List<UserFriendShip> friendships = new ArrayList<>();
        String sql = "SELECT id1, id2, friends_from FROM friendships ORDER BY id1, id2";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                friendships.add(extractFriendship(rs));
            }
            return friendships;
        } catch (SQLException e) {
            throw translateSqlException(e, "findAll friendships");
        }
    }

    /**
     * Deletes a friendship by its composite key.
     * @param id the pair of user IDs
     * @return an {@code Optional} containing the deleted friendship, or empty if not found
     * @throws IllegalArgumentException if {@code id} is null
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Optional<UserFriendShip> delete(Tuple<Integer, Integer> id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        Optional<UserFriendShip> existing = findOne(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }

        String sql = "DELETE FROM friendships WHERE id1 = ? AND id2 = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id.getE1());
            ps.setInt(2, id.getE2());
            ps.executeUpdate();
            return existing;
        } catch (SQLException e) {
            throw translateSqlException(e, "delete friendship");
        }
    }

    /**
     * Updates the timestamp of an existing friendship.
     * @param entity the friendship with updated data
     * @return {@code Optional.empty()} if updated, or {@code Optional.of(entity)} if not found
     * @throws IllegalArgumentException if {@code entity} or its ID is null
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Optional<UserFriendShip> update(UserFriendShip entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Entity and ID cannot be null!");
        }
        validator.validate(entity);

        String sql = "UPDATE friendships SET friends_from = ? WHERE id1 = ? AND id2 = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(entity.getFriendshipDay()));
            ps.setInt(2, entity.getId().getE1());
            ps.setInt(3, entity.getId().getE2());

            int affected = ps.executeUpdate();
            return affected == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw translateSqlException(e, "update friendship");
        }
    }
}