package reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Handles the reservation of train tickets.
 * Prompts the user for reservation details, validates input,
 * inserts a new reservation into the database, and displays the generated PNR.
 */
public class ReservationForm {
    private final String username;
    private final Scanner scanner;

    public ReservationForm(String username) {
        this.username = username;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the reservation process: prompts user for details, validates, and saves to DB.
     */
    public void reserveTicket() {
        System.out.println("\n===== Train Ticket Reservation =====");

        System.out.print("Enter your Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Train Number: ");
        String trainNumber = scanner.nextLine().trim();

        System.out.print("Enter Train Name: ");
        String trainName = scanner.nextLine().trim();

        System.out.print("Enter Class Type (e.g., Sleeper, AC, First): ");
        String classType = scanner.nextLine().trim();

        System.out.print("Enter Journey Date (YYYY-MM-DD): ");
        String journeyDate = scanner.nextLine().trim();

        System.out.print("Enter Source Station: ");
        String source = scanner.nextLine().trim();

        System.out.print("Enter Destination Station: ");
        String destination = scanner.nextLine().trim();

        // Input validation
        if (name.isEmpty() || trainNumber.isEmpty() || trainName.isEmpty() ||
                classType.isEmpty() || journeyDate.isEmpty() ||
                source.isEmpty() || destination.isEmpty()) {
            System.out.println("All fields are required. Reservation not completed.");
            return;
        }

        // Validate journey date format and check if it's in the future
        if (!isValidFutureDate(journeyDate)) {
            System.out.println("Journey date must be in the format YYYY-MM-DD and a future date.");
            return;
        }

        // Save reservation to database and get generated PNR
        Integer pnr = saveReservation(name, trainNumber, trainName, classType, journeyDate, source, destination);

        if (pnr != null) {
            System.out.println("Reservation successful!");
            System.out.println("Your PNR number is: " + pnr);
            System.out.println("Please note your PNR for future reference or cancellation.");
        } else {
            System.out.println("Reservation failed. Please try again.");
        }
    }

    /**
     * Checks if the given date string is a valid date in the future (format: YYYY-MM-DD).
     */
    private boolean isValidFutureDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.isAfter(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Inserts the reservation details into the database and returns the generated PNR.
     * @return The generated PNR number, or null if insertion failed.
     */
    private Integer saveReservation(String name, String trainNumber, String trainName,
                                    String classType, String journeyDate, String source, String destination) {
        String sql = "INSERT INTO reservations (username, name, train_number, train_name, class_type, journey_date, source, destination, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Confirmed')";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, trainNumber);
            pstmt.setString(4, trainName);
            pstmt.setString(5, classType);
            pstmt.setString(6, journeyDate);
            pstmt.setString(7, source);
            pstmt.setString(8, destination);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return null;
            }

            // Retrieve the generated PNR (primary key)
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // The PNR number
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Reservation error: " + e.getMessage());
            return null;
        }
    }
}