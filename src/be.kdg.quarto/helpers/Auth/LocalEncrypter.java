package be.kdg.quarto.helpers.Auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class LocalEncrypter {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);
            String saltBase64 = Base64.getEncoder().encodeToString(salt);

            // Hash the password with the salt
            String hash = hashWithSalt(password, salt);

            // Return salt and hash together
            return saltBase64 + ":" + hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    public static boolean checkPassword(String password, String storedHash) {
        try {
            // Split the stored value to get salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }

            String storedSaltBase64 = parts[0];
            String storedHashValue = parts[1];

            // Decode the salt
            byte[] salt = Base64.getDecoder().decode(storedSaltBase64);

            // Hash the input password with the same salt
            String hashOfInput = hashWithSalt(password, salt);

            // Compare the hashes
            return hashOfInput.equals(storedHashValue);
        } catch (Exception e) {
            return false;
        }
    }
    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}