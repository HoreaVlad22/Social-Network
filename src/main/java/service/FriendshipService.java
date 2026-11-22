package service;

import domain.entities.User;
import domain.entities.UserFriendShip;
import domain.exceptions.RepositoryException;
import repository.Repository;
import utils.StreamUtils;
import utils.Tuple;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing Friendship operations.
 */
public class FriendshipService {
    private final Repository<Tuple<Integer, Integer>, UserFriendShip> friendRepo;
    private final Repository<Integer, User> userRepo;

    /**
     * Creates a FriendshipService backed by the given repositories.
     * @param friendRepo the friendship repository
     * @param userRepo the user repository
     */
    public FriendshipService(Repository<Tuple<Integer, Integer>, UserFriendShip> friendRepo, Repository<Integer, User> userRepo) {
        this.friendRepo = friendRepo;
        this.userRepo = userRepo;
    }

    /**
     * Adds a friendship between two users.
     * @param id1 ID of the first user
     * @param id2 ID of the second user
     * @throws RepositoryException if users are identical, don't exist, or friendship already exists
     */
    public void addFriendship(int id1, int id2) {
        if (id1 == id2) throw new RepositoryException("Nu te poti imprieteni cu tine insuti!");

        userRepo.findOne(id1).orElseThrow(() -> new RepositoryException("Unul sau ambii utilizatori nu exista!"));
        userRepo.findOne(id2).orElseThrow(() -> new RepositoryException("Unul sau ambii utilizatori nu exista!"));

        Tuple<Integer, Integer> pair = new Tuple<>(Math.min(id1, id2), Math.max(id1, id2));
        friendRepo.add(new UserFriendShip(pair, LocalDateTime.now()))
                .ifPresent(existing -> {
                    throw new RepositoryException("Prietenia exista deja!");
                });
    }

    /**
     * Removes the friendship between two users.
     * @param id1 ID of the first user
     * @param id2 ID of the second user
     * @throws RepositoryException if the friendship does not exist
     */
    public void removeFriendship(int id1, int id2) {
        Tuple<Integer, Integer> pair = new Tuple<>(Math.min(id1, id2), Math.max(id1, id2));
        friendRepo.delete(pair)
                .orElseThrow(() -> new RepositoryException("Prietenia nu exista!"));
    }

    /**
     * Removes all friendships involving the specified user.
     * @param userId ID of the user whose friendships should be deleted
     */
    public void removeFriendshipsOfUser(int userId) {
        List<Tuple<Integer, Integer>> toDelete = StreamUtils.toStream(friendRepo.findAll())
                .filter(f -> f.getId().getE1() == userId || f.getId().getE2() == userId)
                .map(UserFriendShip::getId)
                .collect(Collectors.toList());

        toDelete.forEach(friendRepo::delete);
    }

    /**
     * Retrieves all friends of the specified user.
     * @param userId ID of the user
     * @return a list of {@code User} entities who are friends with the given user
     */
    public List<User> getFriendsOfUser(int userId) {
        return StreamUtils.toStream(friendRepo.findAll())
                .filter(f -> f.getId().getE1() == userId || f.getId().getE2() == userId)
                .map(f -> f.getId().getE1() == userId ? f.getId().getE2() : f.getId().getE1())
                .distinct()
                .map(userRepo::findOne)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all friendships.
     * @return an iterable of all friendships
     */
    public Iterable<UserFriendShip> getAllFriendships() {
        return friendRepo.findAll();
    }
}