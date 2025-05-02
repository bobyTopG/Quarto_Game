package be.kdg.quarto.helpers.Auth;

import be.kdg.quarto.helpers.DbConnection;
import be.kdg.quarto.model.Human;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Static helper class for authentication operations.
 * This approach is suitable for a single-player game where only one user is logged in at a time.
 */
public class AuthHelper {
    private static Human loggedInPlayer;

    // Private constructor to prevent instantiation
    private AuthHelper() {
        // This class shouldn't be instantiated
    }

    public static boolean isLoggedIn() {
        return loggedInPlayer != null;
    }

    public static Human getLoggedInPlayer() {
        return loggedInPlayer;
    }

    public static Human getGuestPlayer() {
        //one of the presets in the fill script
        return new Human("Guest", null);
    }

    public static Human login(String username, String password) throws AuthException {
        try (PreparedStatement stmt = DbConnection.connection.prepareStatement("SELECT player_id, name, password FROM players WHERE name = ?")) {
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
                    throw new AuthException("User not found");

                }
            } else {
                throw new AuthException("User not found");
            }
        } catch (SQLException | NullPointerException e) {
            throw new AuthException("Database error during login");
        }
    }

    public static void logout() {
        loggedInPlayer = null;
    }


    public static Human register(String username, String password) throws AuthException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthException("Username cannot be empty");
        }
        if (password == null || password.length() < 4) {
            throw new AuthException("Password must be at least 4 characters");
        }

        try (PreparedStatement checkStmt = DbConnection.connection.prepareStatement("SELECT COUNT(*) FROM players WHERE name = ?")) {
            checkStmt.setString(1, username);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next() && checkRs.getInt(1) > 0) {
                throw new AuthException("Username already exists");
            }

            String hashedPassword = LocalEncrypter.hashPassword(password);

            String insertQuery = "INSERT INTO players (name, password) VALUES (?, ?) RETURNING player_id";
            PreparedStatement insertStmt = DbConnection.connection.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);

            ResultSet insertRs = insertStmt.executeQuery();

            if (insertRs.next()) {
                int playerId = insertRs.getInt("player_id");
                Human newHuman = new Human(playerId, username, hashedPassword);

                loggedInPlayer = newHuman;
                return newHuman;
            } else {
                throw new AuthException("Database error during login");
            }
        } catch (SQLException | NullPointerException e) {
            throw new AuthException("Database error during login");
        }
    }
}