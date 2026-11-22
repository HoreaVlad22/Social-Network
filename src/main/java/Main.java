import domain.entities.User;
import domain.entities.UserFriendShip;
import domain.exceptions.ValidatorException;
import domain.validator.FriendshipValidator;
import domain.validator.UserValidator;
import domain.validator.Validator;
import repository.Repository;
import repository.RepositoryFactory;
import service.CommunityService;
import service.FriendshipService;
import service.UserService;
import ui.ConsoleUI;
import utils.Tuple;

/**
 * Entry point of the Social Network application.
 */
public class Main {
    public static void main(String[] args) {
        RepositoryFactory factory = new RepositoryFactory("src/main/resources/config.properties");

        Validator<Integer, User> userValidator = new UserValidator().andThen(user -> {
            StringBuilder errors = new StringBuilder();
            if (user.getPassword() == null || user.getPassword().length() < 4) {
                errors.append("Password must have at least 4 characters.\n");
            }
            if (!errors.isEmpty()) {
                throw new ValidatorException(errors.toString());
            }
        });

        Repository<Integer, User> userRepo = factory.createUserRepository(userValidator);
        Repository<Tuple<Integer, Integer>, UserFriendShip> friendRepo = factory.createFriendshipRepository(new FriendshipValidator());

        UserService userService = new UserService(userRepo);
        FriendshipService friendshipService = new FriendshipService(friendRepo, userRepo);
        CommunityService communityService = new CommunityService(userService, friendshipService);

        ConsoleUI ui = new ConsoleUI(userService, friendshipService, communityService);
        ui.run();
    }
}