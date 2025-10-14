package reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing SQLite database connection and initialization.
 */
public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:reservation.db?busy_timeout=5000";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY," +
                "password TEXT NOT NULL" +
                ");";
        String createReservationsTable = "CREATE TABLE IF NOT EXISTS reservations (" +
                "pnr INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "train_number TEXT NOT NULL," +
                "train_name TEXT NOT NULL," +
                "class_type TEXT NOT NULL," +
                "journey_date TEXT NOT NULL," +
                "source TEXT NOT NULL," +
                "destination TEXT NOT NULL," +
                "status TEXT NOT NULL DEFAULT 'Confirmed'," +
                "FOREIGN KEY(username) REFERENCES users(username)" +
                ");";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");
            stmt.execute(createUsersTable);
            stmt.execute(createReservationsTable);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}