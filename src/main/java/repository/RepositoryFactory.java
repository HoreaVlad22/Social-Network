package repository;

import domain.entities.User;
import domain.entities.UserFriendShip;
import domain.validator.Validator;
import utils.Tuple;

import java.io.FileReader;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/**
 * Factory for instantiating repositories based on configuration,
 * using a Map of creator functions instead of switch statements.
 */
public class RepositoryFactory {
    private final RepositoryType type;
    private final Properties properties;

    private final Map<RepositoryType, Function<Validator<Integer, User>, Repository<Integer, User>>> userCreators;
    private final Map<RepositoryType, Function<Validator<Tuple<Integer, Integer>, UserFriendShip>, Repository<Tuple<Integer, Integer>, UserFriendShip>>> friendshipCreators;

    /**
     * Initializes the factory by reading the repository configuration from the given file.
     * @param configFilePath path to the {@code config.properties} file
     * @throws RuntimeException if the configuration file cannot be read or is invalid
     */
    public RepositoryFactory(String configFilePath) {
        properties = new Properties();
        try (FileReader reader = new FileReader(configFilePath)) {
            properties.load(reader);
            this.type = RepositoryType.valueOf(properties.getProperty("repository.type").toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Configuration error", e);
        }

        this.userCreators = Map.of(
                RepositoryType.MEMORY, InMemoryRepository::new,
                RepositoryType.FILE, v -> new UserFileRepository(properties.getProperty("users.file"), v),
                RepositoryType.DATABASE, v -> new UserDbRepository(v, properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"))
        );

        this.friendshipCreators = Map.of(
                RepositoryType.MEMORY, InMemoryRepository::new,
                RepositoryType.FILE, v -> new FriendshipFileRepository(properties.getProperty("friendships.file"), v),
                RepositoryType.DATABASE, v -> new FriendshipDbRepository(v, properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"))
        );
    }

    /**
     * Creates a User repository corresponding to the configured {@link RepositoryType}.
     * @param validator the validator for User entities
     * @return the instantiated User repository
     * @throws IllegalArgumentException if the configured type is unknown
     */
    public Repository<Integer, User> createUserRepository(Validator<Integer, User> validator) {
        Function<Validator<Integer, User>, Repository<Integer, User>> creator = userCreators.get(this.type);
        if (creator == null) throw new IllegalArgumentException("Unknown repository type: " + this.type);
        return creator.apply(validator);
    }

    /**
     * Creates a Friendship repository corresponding to the configured {@link RepositoryType}.
     * @param validator the validator for UserFriendShip entities
     * @return the instantiated Friendship repository
     * @throws IllegalArgumentException if the configured type is unknown
     */
    public Repository<Tuple<Integer, Integer>, UserFriendShip> createFriendshipRepository(Validator<Tuple<Integer, Integer>, UserFriendShip> validator) {
        Function<Validator<Tuple<Integer, Integer>, UserFriendShip>, Repository<Tuple<Integer, Integer>, UserFriendShip>> creator = friendshipCreators.get(this.type);
        if (creator == null) throw new IllegalArgumentException("Unknown repository type: " + this.type);
        return creator.apply(validator);
    }
}