package ui;

import service.CommunityService;
import service.FriendshipService;

import java.util.Scanner;

/**
 * Console UI for managing friendships and community statistics.
 */
public class FriendshipUI {
    private final FriendshipService friendshipService;
    private final CommunityService communityService;
    private final Scanner scanner;

    public FriendshipUI(FriendshipService fService, CommunityService cService, Scanner scanner) {
        this.friendshipService = fService;
        this.communityService = cService;
        this.scanner = scanner;
    }

    /**
     * Runs the interactive friendship menu loop.
     */
    public void run() {
        while (true) {
            System.out.println("\n--- PRIETENII ---");
            System.out.println("1. Adauga | 2. Sterge | 3. Afiseaza | 4. Numar Comunitati | 5. Cea mai sociabila | 0. Inapoi");
            System.out.print("Alege: ");

            try {
                switch (scanner.nextLine().trim()) {
                    case "1":
                        System.out.print("ID 1: "); int id1 = Integer.parseInt(scanner.nextLine());
                        System.out.print("ID 2: "); int id2 = Integer.parseInt(scanner.nextLine());
                        friendshipService.addFriendship(id1, id2);
                        System.out.println("Prietenie salvata!"); break;
                    case "2":
                        System.out.print("ID 1: "); int r1 = Integer.parseInt(scanner.nextLine());
                        System.out.print("ID 2: "); int r2 = Integer.parseInt(scanner.nextLine());
                        friendshipService.removeFriendship(r1, r2);
                        System.out.println("Prietenie stearsa!"); break;
                    case "3":
                        friendshipService.getAllFriendships().forEach(System.out::println);
                        break;
                    case "4":
                        System.out.println("Numar total de comunitati: " + communityService.getNumberOfCommunities());
                        break;
                    case "5":
                        System.out.println("Cea mai sociabila comunitate are ID-urile: " + communityService.getMostSociableCommunity());
                        break;
                    case "0": return;
                    default: System.out.println("Optiune invalida!"); break;
                }
            } catch (domain.exceptions.ValidatorException | domain.exceptions.RepositoryException | domain.exceptions.FormattingException e) {
                System.out.println("Eroare la operatiune: " + e.getMessage());
            } catch (UnsupportedOperationException e) {
                System.out.println("Eroare persistenta: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Eroare: ID-ul trebuie sa fie un numar intreg!");
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare argument invalid: " + e.getMessage());
            }
        }
    }
}