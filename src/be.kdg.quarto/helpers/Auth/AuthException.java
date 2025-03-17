package be.kdg.quarto.helpers.Auth;

public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
    AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}