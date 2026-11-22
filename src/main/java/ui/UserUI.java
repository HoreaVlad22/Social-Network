package ui;

import service.FriendshipService;
import service.UserService;

import java.util.Scanner;

/**
 * Console UI for managing users.
 */
public class UserUI {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final Scanner scanner;

    public UserUI(UserService userService, FriendshipService friendshipService, Scanner scanner) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.scanner = scanner;
    }

    /**
     * Runs the interactive user menu loop.
     */
    public void run() {
        while (true) {
            System.out.println("\n--- MENIU UTILIZATORI ---");
            System.out.println("1. Adauga | 2. Sterge | 3. Afiseaza | 0. Inapoi");
            System.out.print("Alege: ");
            String opt = scanner.nextLine().trim();

            try {
                switch (opt) {
                    case "1":
                        System.out.print("ID: "); int id = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nume: "); String name = scanner.nextLine();
                        System.out.print("Prenume: "); String surname = scanner.nextLine();
                        System.out.print("User: "); String user = scanner.nextLine();
                        System.out.print("Pass: "); String pass = scanner.nextLine();
                        userService.addUser(id, name, surname, user, pass);
                        System.out.println("Adaugat cu succes!"); break;
                    case "2":
                        System.out.print("ID: "); int delId = Integer.parseInt(scanner.nextLine());
                        friendshipService.removeFriendshipsOfUser(delId);
                        userService.removeUser(delId);
                        System.out.println("Sters cu succes!"); break;
                    case "3":
                        userService.getAllUsers().forEach(u ->
                                System.out.println(u.getId() + " - " + u.getName() + " " + u.getSurname())
                        );
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