package reservation;

import java.util.Scanner;

/**
 * Main entry point for the Online Reservation System.
 * After login, lets user choose between reservation and cancellation.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database tables (users, reservations)
        DatabaseUtil.initializeDatabase();

        Scanner scanner = new Scanner(System.in);
        LoginForm loginForm = new LoginForm();

        System.out.println("===== Welcome to the Online Reservation System =====");

        String loggedInUser = null;
        while (loggedInUser == null) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    loginForm.register();
                    break;
                case "2":
                    loggedInUser = loginForm.login();
                    break;
                case "3":
                    System.out.println("Thank you for using Online Reservation System.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        // Main menu after successful login
        while (true) {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Reserve a Ticket");
            System.out.println("2. Cancel a Ticket");
            System.out.println("3. Logout & Exit");
            System.out.print("Enter your choice: ");
            String mainChoice = scanner.nextLine().trim();

            switch (mainChoice) {
                case "1":
                    ReservationForm reservationForm = new ReservationForm(loggedInUser);
                    reservationForm.reserveTicket();
                    break;
                case "2":
                    CancellationForm cancellationForm = new CancellationForm(loggedInUser);
                    cancellationForm.cancelReservation();
                    break;
                case "3":
                    System.out.println("Logging out. Thank you for using the system!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}