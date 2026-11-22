package utils;

import domain.entities.User;
import java.util.function.Predicate;

/**
 * Utility class providing reusable search criteria (predicates) for User entities.
 */
public class UserCriteria {

    /**
     * Matches users whose name equals the given name (case-insensitive).
     * @param name the name to match
     * @return a {@code Predicate<User>}
     */
    public static Predicate<User> hasName(String name) {
        return user -> user.getName().equalsIgnoreCase(name);
    }

    /**
     * Matches users whose surname equals the given surname (case-insensitive).
     * @param surname the surname to match
     * @return a {@code Predicate<User>}
     */
    public static Predicate<User> hasSurname(String surname) {
        return user -> user.getSurname().equalsIgnoreCase(surname);
    }

    /**
     * Matches users whose username starts with the given prefix (case-insensitive).
     * @param prefix the prefix to check
     * @return a {@code Predicate<User>}
     */
    public static Predicate<User> usernameStartsWith(String prefix) {
        return user -> user.getUsername().toLowerCase().startsWith(prefix.toLowerCase());
    }

    /**
     * Checks whether a user has a strong password (at least 8 characters).
     * Can be used directly as a static method reference: {@code UserCriteria::hasStrongPassword}.
     * @param user the user to check
     * @return {@code true} if the password has at least 8 characters, {@code false} otherwise
     */
    public static boolean hasStrongPassword(User user) {
        return user.getPassword() != null && user.getPassword().length() >= 8;
    }
}