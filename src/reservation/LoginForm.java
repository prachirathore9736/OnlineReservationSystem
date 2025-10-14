package reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Handles the login functionality for the Online Reservation System.
 * Users are prompted for username and password, which are checked against the database.
 * Passwords are stored in plain text for demonstration purposes only (not recommended for production).
 */
public class LoginForm {
    private Scanner scanner;

    public LoginForm() {
        scanner = new Scanner(System.in);
    }

    /**
     * Prompts the user to log in and validates credentials.
     * @return the username if authentication is successful, null otherwise.
     */
    public String login() {
        System.out.println("===== User Login =====");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        if (validateCredentials(username, password)) {
            System.out.println("Login successful! Welcome, " + username + "!");
            return username;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return null;
        }
    }

    /**
     * Validates the username and password against the database.
     * @param username the entered username
     * @param password the entered password
     * @return true if credentials are valid, false otherwise
     */
    private boolean validateCredentials(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // For this demo, compare plain text passwords
                return password.equals(storedPassword);
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Optionally, a simple registration function for first-time users.
     */
    public void register() {
        System.out.println("===== New User Registration =====");
        System.out.print("Choose Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Choose Password: ");
        String password = scanner.nextLine().trim();

        if (addUserToDatabase(username, password)) {
            System.out.println("Registration successful! You can now log in.");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }

    /**
     * Adds a new user to the database.
     * @param username the new username
     * @param password the new password
     * @return true if registration succeeds, false otherwise
     */
    private boolean addUserToDatabase(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Likely cause: username already exists
            // System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }
}