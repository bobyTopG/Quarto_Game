package be.kdg.quarto.view.auth.RegisterView;

import be.kdg.quarto.helpers.Auth.AuthException;
import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.model.Human;
import be.kdg.quarto.view.ChooseAIView.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIView.ChooseAIView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.scene.control.Alert;

public class RegisterPresenter {

    private RegisterView view;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getBackButton().setOnAction(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });

        view.getContinueAsGuestButton().setOnAction(event -> {
            ChooseAIView chooseAIView = new ChooseAIView();
            view.getScene().setRoot(chooseAIView);
            new ChooseAIPresenter(chooseAIView);
        });
        view.getRegisterButton().setOnAction(event -> {
            registerUser();
        });
    }
    private void registerUser() {
        String username = view.getUsernameTextField().getText();
        String password = view.getPasswordTextField().getText();
        String confirmPassword = view.getConfirmPasswordTextField().getText();

        // Validate input fields
        if (username == null || username.trim().isEmpty()) {
            showAlert("Registration Error", "Username cannot be empty.");
            return;
        }

        if (password == null || password.isEmpty()) {
            showAlert("Registration Error", "Password cannot be empty.");
            return;
        }

        if (password.length() < 4) {
            showAlert("Registration Error", "Password must be at least 4 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Registration Error", "Passwords do not match.");
            return;
        }

        // Attempt to register the user
        try {
            Human registeredUser = AuthHelper.register(username, password);

            // If successful, proceed to Choose AI View
            showAlert("Registration Successful", "Welcome, " + registeredUser.getName() + "!");

            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);

        } catch (AuthException e) {
            showAlert("Registration Error", e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}
