package domain.entities;

/**
 * Represents a User in the social network.
 */
public class User extends Entity<Integer> {
    private String name;
    private String surname;
    private String username;
    private String password;

    public User(int id, String name, String surname, String username, String password) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}