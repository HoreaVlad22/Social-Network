package service;

import domain.entities.User;
import utils.StreamUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Service for calculating community components and statistics using the Stream API.
 */
public class CommunityService {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public CommunityService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    /**
     * Builds the undirected adjacency list of the social network graph.
     * @return a map from user ID to the list of neighboring user IDs
     */
    private Map<Integer, List<Integer>> getAdjacencyList() {
        Set<Integer> validUsers = StreamUtils.toStream(userService.getAllUsers())
                .map(User::getId)
                .collect(Collectors.toSet());

        Map<Integer, List<Integer>> adj = validUsers.stream()
                .collect(Collectors.toMap(id -> id, id -> new ArrayList<>()));

        Map<Integer, List<Integer>> edges = StreamSupport.stream(friendshipService.getAllFriendships().spliterator(), false)
                .filter(f -> validUsers.contains(f.getId().getE1()) && validUsers.contains(f.getId().getE2()))
                .flatMap(f -> Stream.of(
                        Map.entry(f.getId().getE1(), f.getId().getE2()),
                        Map.entry(f.getId().getE2(), f.getId().getE1())
                ))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        edges.forEach(adj::put);
        return adj;
    }

    /**
     * Finds all distinct connected components (communities) in the graph.
     * @param adj the adjacency list of the graph
     * @return a list of sets, each set representing a community of user IDs
     */
    private List<Set<Integer>> getAllCommunities(Map<Integer, List<Integer>> adj) {
        return adj.keySet().stream()
                .map(node -> exploreComponent(node, adj, new HashSet<>()))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Recursively explores all nodes reachable from {@code node}.
     * @param node starting user ID
     * @param adj adjacency list
     * @param visited set of already visited user IDs
     * @return the complete set of user IDs in the connected component
     */
    private Set<Integer> exploreComponent(int node, Map<Integer, List<Integer>> adj, Set<Integer> visited) {
        if (!visited.add(node)) {
            return visited;
        }
        adj.getOrDefault(node, Collections.emptyList()).stream()
                .filter(neighbor -> !visited.contains(neighbor))
                .forEach(neighbor -> exploreComponent(neighbor, adj, visited));
        return visited;
    }

    /**
     * Computes the total number of communities (connected components) in the network.
     * @return the number of communities
     */
    public int getNumberOfCommunities() {
        Map<Integer, List<Integer>> adj = getAdjacencyList();
        return (int) getAllCommunities(adj).stream().count();
    }

    /**
     * Finds the most sociable community (the connected component with the longest simple path).
     * @return a list of user IDs belonging to the most sociable community, or an empty list if none exist
     */
    public List<Integer> getMostSociableCommunity() {
        Map<Integer, List<Integer>> adj = getAdjacencyList();
        return getAllCommunities(adj).stream()
                .max(Comparator.comparingInt(community -> getLongestPathInCommunity(community, adj)))
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    /**
     * Computes the length of the longest simple path within a given community.
     * @param community set of user IDs in the community
     * @param adj adjacency list
     * @return length of the longest simple path
     */
    private int getLongestPathInCommunity(Set<Integer> community, Map<Integer, List<Integer>> adj) {
        return community.stream()
                .mapToInt(node -> dfsLongestPath(node, Set.of(node), adj))
                .max()
                .orElse(0);
    }

    /**
     * Computes the longest simple path starting from {@code node} without revisiting nodes in {@code visited}.
     * @param node current user ID
     * @param visited set of visited user IDs on the current path
     * @param adj adjacency list
     * @return length of the longest path starting from {@code node}
     */
    private int dfsLongestPath(int node, Set<Integer> visited, Map<Integer, List<Integer>> adj) {
        int maxDepth = adj.getOrDefault(node, Collections.emptyList()).stream()
                .filter(neighbor -> !visited.contains(neighbor))
                .mapToInt(neighbor -> {
                    Set<Integer> nextVisited = new HashSet<>(visited);
                    nextVisited.add(neighbor);
                    return dfsLongestPath(neighbor, nextVisited, adj);
                })
                .max()
                .orElse(0);

        return maxDepth + 1;
    }
}