package ui;

import service.CommunityService;
import service.FriendshipService;
import service.UserService;
import java.util.Scanner;

public class ConsoleUI {
    private final UserUI userUI;
    private final FriendshipUI friendshipUI;
    private final Scanner scanner;

    public ConsoleUI(UserService uServ, FriendshipService fServ, CommunityService cServ) {
        this.scanner = new Scanner(System.in);
        this.userUI = new UserUI(uServ, fServ, scanner);
        this.friendshipUI = new FriendshipUI(fServ, cServ, scanner);
    }

    public void run() {
        while (true) {
            System.out.println("\n=== SOCIAL NETWORK ===");
            System.out.println("1. Utilizatori | 2. Prietenii | 0. Iesire");
            System.out.print("Alege: ");
            switch (scanner.nextLine().trim()) {
                case "1": userUI.run(); break;
                case "2": friendshipUI.run(); break;
                case "0": return;
                default: System.out.println("Optiune invalida!"); break;
            }
        }
    }
}