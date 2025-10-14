package reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Handles ticket cancellation by PNR number.
 * Verifies the PNR, shows reservation details, and allows cancellation.
 */
public class CancellationForm {
    private final String username;
    private final Scanner scanner;

    public CancellationForm(String username) {
        this.username = username;
        this.scanner = new Scanner(System.in);
    }

    public void cancelReservation() {
        System.out.println("\n===== Ticket Cancellation =====");
        System.out.print("Enter your PNR number: ");
        String pnrInput = scanner.nextLine().trim();

        int pnr;
        try {
            pnr = Integer.parseInt(pnrInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid PNR number format.");
            return;
        }

        // Check if PNR exists for this user and fetch reservation details
        String query = "SELECT * FROM reservations WHERE pnr = ? AND username = ?";
        boolean found = false;
        boolean alreadyCancelled = false;
        String status = "";
        try (
            Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setInt(1, pnr);
            pstmt.setString(2, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    found = true;
                    status = rs.getString("status");
                    System.out.println("Reservation found:");
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Train Number: " + rs.getString("train_number"));
                    System.out.println("Train Name: " + rs.getString("train_name"));
                    System.out.println("Class: " + rs.getString("class_type"));
                    System.out.println("Journey Date: " + rs.getString("journey_date"));
                    System.out.println("From: " + rs.getString("source"));
                    System.out.println("To: " + rs.getString("destination"));
                    System.out.println("Status: " + status);

                    if ("Cancelled".equalsIgnoreCase(status)) {
                        alreadyCancelled = true;
                        System.out.println("This reservation is already cancelled.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Cancellation error: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("No reservation found with PNR " + pnr + " for your account.");
            return;
        }
        if (alreadyCancelled) {
            return;
        }

        System.out.print("Are you sure you want to cancel this ticket? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes") || confirm.equals("y")) {
            // Sleep for a brief moment to avoid locks (optional)
            try { Thread.sleep(100); } catch (InterruptedException ex) {}
            // Now, update on a *completely new* connection:
            if (updateCancellationStatus(pnr)) {
                System.out.println("Ticket cancelled successfully.");
            } else {
                System.out.println("Cancellation failed. Please try again.");
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    /**
     * Updates the reservation status to 'Cancelled' in the database.
     * Uses a separate connection to avoid potential locking issues.
     */
    private boolean updateCancellationStatus(int pnr) {
        String updateSql = "UPDATE reservations SET status = 'Cancelled' WHERE pnr = ?";
        try (
            Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(updateSql)
        ) {
            pstmt.setInt(1, pnr);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update error: " + e.getMessage());
            return false;
        }
    }
}