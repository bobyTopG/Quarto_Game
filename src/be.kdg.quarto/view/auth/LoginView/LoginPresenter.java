package be.kdg.quarto.view.auth.LoginView;

import be.kdg.quarto.helpers.Auth.AuthException;
import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.model.Human;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static be.kdg.quarto.helpers.CreateHelper.showAlert;

public class LoginPresenter {

    private LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
        updateView();
        addEventHandlers();
    }


    private void addEventHandlers() {
        // Back button handler - return to Start screen
        view.getBackButton().setOnAction(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });

        // Login button handler - attempt to login
        view.getLoginButton().setOnAction(event -> {
            loginUser();
        });
    }

    /**
     * Handles the user login process
     */
    private void loginUser() {
        String username = view.getUsernameTextField().getText();
        String password = view.getPasswordTextField().getText();

        // Validate input fields
        if (username == null || username.trim().isEmpty()) {
            showAlert("Login Error", "Username cannot be empty.", true);
            return;
        }

        if (password == null || password.isEmpty()) {
            showAlert("Login Error", "Password cannot be empty.", true);
            return;
        }

        // Attempt to log in
        try {
            Human loggedInUser = AuthHelper.login(username, password);
            // If successful, proceed to Choose AI View
            showAlert("Login Successful", "Welcome back, " + loggedInUser.getName() + "!", false);

            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);

        } catch (AuthException e) {
            // Check the exception message to determine the specific error
            if (e.getMessage().equals("User not found")) {
                showAlert("User not Found", "No account found with that username. Please check your spelling or register a new account.", true);
            } else {
                showAlert("Authentication Error", "Failed to log in: " + e.getMessage(), true);
            }
        } catch (Exception e) {
            showAlert("Login Error", "An unexpected error occurred: " + e.getMessage(), true);
        }
    }


    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}