package be.kdg.quarto.view.auth.LoginView;

import be.kdg.quarto.helpers.Auth.AuthException;
import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.helpers.DbConnection;
import be.kdg.quarto.model.Human;
import be.kdg.quarto.view.ChooseAIView.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIView.ChooseAIView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import be.kdg.quarto.view.UiSettings;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;

public class LoginPresenter {

    private LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
        updateView();
        addEventHandlers();
    }

    UiSettings uiSettings = new UiSettings();

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
            showAlert("Login Error", "Username cannot be empty.");
            return;
        }

        if (password == null || password.isEmpty()) {
            showAlert("Login Error", "Password cannot be empty.");
            return;
        }

        // Attempt to login
        try {
            Human loggedInUser = AuthHelper.login(username, password);

            // If successful, proceed to Choose AI View
            showAlert("Login Successful", "Welcome back, " + loggedInUser.getName() + "!");

            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);

        } catch (AuthException e) {
            showAlert("Login Error", e.getMessage());
        }
    }

    /**
     * Helper method to display alerts to the user
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}