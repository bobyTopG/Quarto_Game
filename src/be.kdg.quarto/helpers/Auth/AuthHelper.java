package be.kdg.quarto.helpers.Auth;

import be.kdg.quarto.helpers.DbConnection;
import be.kdg.quarto.model.Human;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Static helper class for authentication operations.
 * This approach is suitable for a single-player game where only one user is logged in at a time.
 */
public class AuthHelper {
    private static Human loggedInPlayer;
    private static Connection dbConnection;

    // Static initialization block to set up database connection
    static {
        dbConnection = DbConnection.connection;
    }

    // Private constructor to prevent instantiation
    private AuthHelper() {
        // This class shouldn't be instantiated
    }

    /**
     * Check if a user is currently logged in
     * @return true if a user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return loggedInPlayer != null;
    }

    /**
     * Get the currently logged in player
     * @return the logged in Human object, or null if no one is logged in
     */
    public static Human getLoggedInPlayer() {
        return loggedInPlayer;
    }

    /**
     * Attempt to log in with the provided credentials
     * @param username The username to log in with
     * @param password The password to authenticate with
     * @return The logged in Human object
     * @throws AuthException If login fails for any reason
     */
    public static Human login(String username, String password) throws AuthException {
        try {
            String query = "SELECT player_id, name, password FROM players WHERE name = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                if (LocalEncrypter.checkPassword(password, hashedPassword)) {
                    int playerId = rs.getInt("player_id");
                    String playerName = rs.getString("name");

                    Human human = new Human(playerId, playerName, hashedPassword);
                    loggedInPlayer = human;
                    return human;
                } else {
                    throw new AuthException("Invalid password");
                }
            } else {
                throw new AuthException("User not found");
            }
        } catch (SQLException e) {
            throw new AuthException("Database error during login: " + e.getMessage());
        }
    }

    /**
     * Log out the current user
     */
    public static void logout() {
        loggedInPlayer = null;
    }

    /**
     * Register a new user
     * @param username The username for the new account
     * @param password The password for the new account
     * @return The newly created Human object
     * @throws AuthException If registration fails for any reason
     */
    public static Human register(String username, String password) throws AuthException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthException("Username cannot be empty");
        }
        if (password == null || password.length() < 4) {
            throw new AuthException("Password must be at least 4 characters");
        }

        try {
            String checkQuery = "SELECT COUNT(*) FROM players WHERE name = ?";
            PreparedStatement checkStmt = dbConnection.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next() && checkRs.getInt(1) > 0) {
                throw new AuthException("Username already exists");
            }

            String hashedPassword = LocalEncrypter.hashPassword(password);

            String insertQuery = "INSERT INTO players (name, password) VALUES (?, ?) RETURNING player_id";
            PreparedStatement insertStmt = dbConnection.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);

            ResultSet insertRs = insertStmt.executeQuery();

            if (insertRs.next()) {
                int playerId = insertRs.getInt("player_id");
                Human newHuman = new Human(playerId, username, hashedPassword);

                loggedInPlayer = newHuman;
                return newHuman;
            } else {
                throw new AuthException("Failed to retrieve player ID after registration");
            }
        } catch (SQLException e) {
            throw new AuthException("Database error during registration: " + e.getMessage());
        }
    }

    /**
     * Change the password for the currently logged in user
     * @param currentPassword The current password for verification
     * @param newPassword The new password to set
     * @throws AuthException If the password change fails for any reason
     */
    public static void changePassword(String currentPassword, String newPassword) throws AuthException {
        if (!isLoggedIn()) {
            throw new AuthException("No user is logged in");
        }

        try {
            String query = "SELECT password FROM players WHERE player_id = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setInt(1, loggedInPlayer.getId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");

                if (!LocalEncrypter.checkPassword(currentPassword, storedHash)) {
                    throw new AuthException("Current password is incorrect");
                }
            } else {
                throw new AuthException("User not found");
            }
        } catch (SQLException e) {
            throw new AuthException("Database error: " + e.getMessage());
        }

        if (newPassword == null || newPassword.length() < 4) {
            throw new AuthException("New password must be at least 4 characters");
        }

        try {
            String hashedNewPassword = LocalEncrypter.hashPassword(newPassword);

            String query = "UPDATE players SET password = ? WHERE player_id = ?";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setString(1, hashedNewPassword);
            stmt.setInt(2, loggedInPlayer.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                loggedInPlayer.setPassword(hashedNewPassword);
            } else {
                throw new AuthException("Failed to update password");
            }
        } catch (SQLException e) {
            throw new AuthException("Database error during password change: " + e.getMessage());
        }
    }
}