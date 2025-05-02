package be.kdg.quarto.view.auth.RegisterView;

import be.kdg.quarto.helpers.Auth.AuthException;
import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.model.Human;
import be.kdg.quarto.view.ChooseAIScreen.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIScreen.ChooseAIView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.scene.control.Alert;

import static be.kdg.quarto.helpers.CreateHelper.showAlert;

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
            new ChooseAIPresenter(chooseAIView,false);
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
            showAlert("Registration Error", "Username cannot be empty.", true);
            return;
        }

        if (password == null || password.isEmpty()) {
            showAlert("Registration Error", "Password cannot be empty.", true);
            return;
        }

        if (password.length() < 4) {
            showAlert("Registration Error", "Password must be at least 4 characters.", true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Registration Error", "Passwords do not match.", true);
            return;
        }

        // Attempt to register the user
        try {
            Human registeredUser = AuthHelper.register(username, password);

            // If successful, proceed to Start View
            showAlert("Registration Successful", "Welcome, " + registeredUser.getName() + "!", false);

            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);

        } catch (AuthException e) {
            // Check specific error messages from the AuthHelper.register method
            if (e.getMessage().equals("Username already exists")) {
                showAlert("Registration Error", "This username is already taken. Please choose a different username.", true);
            } else if (e.getMessage().contains("Database error")) {
                showAlert("Registration Error", "A system error occurred. Please try again later.", true);
            } else {
                // For other AuthExceptions, display the message directly
                showAlert("Registration Error", e.getMessage(), true);
            }
        } catch (Exception e) {
            // Catch any unexpected exceptions
            showAlert("Registration Error", "An unexpected error occurred: ", true);
        }
    }


    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}
