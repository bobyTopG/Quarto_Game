package be.kdg.quarto.view.auth.RegisterView;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import be.kdg.quarto.helpers.FontHelper;
import be.kdg.quarto.helpers.CreateHelper;
import javafx.stage.Stage;

public class RegisterView extends BorderPane {

    private Label registerLabel;
    private TextField usernameTextField;
    private PasswordField passwordTextField;
    private PasswordField confirmPasswordTextField;
    private Button registerButton;
    private Button continueAsGuestButton;
    private Button backButton;

    private HBox mainHBox;


    public RegisterView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        registerLabel = CreateHelper.createLabel("Register", "main-title");
        registerLabel.setFont(FontHelper.getLargeFont());

        usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        usernameTextField.getStyleClass().addAll("default-button", "white-text-field");

        passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");
        passwordTextField.getStyleClass().addAll("default-button", "white-text-field");

        confirmPasswordTextField = new PasswordField();
        confirmPasswordTextField.setPromptText("Confirm Password");
        confirmPasswordTextField.getStyleClass().addAll("default-button", "white-text-field");

        registerButton = CreateHelper.createButton("Register", new String[] {"green-button", "default-button"});
        continueAsGuestButton = CreateHelper.createButton("Continue as Guest", new String[] {"orange-button", "default-button"});
        backButton = CreateHelper.createButton("Back", new String[] {"red-button", "default-button"});

        this.setTop(registerLabel);
    }

    private void layoutNodes() {

        HBox buttonHBox = CreateHelper.createHBox("login-button-hbox");
        buttonHBox.getChildren().addAll(registerButton, backButton);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(10);

        VBox vbox = CreateHelper.createVBox("login-main-vbox");
        vbox.getChildren().addAll(registerLabel, usernameTextField, passwordTextField, confirmPasswordTextField, buttonHBox, continueAsGuestButton);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);


        mainHBox = CreateHelper.createHBox("login-main-hbox");
        mainHBox.getChildren().addAll(vbox);
        mainHBox.setAlignment(Pos.CENTER);
        this.setCenter(mainHBox);

    }

     TextField getUsernameTextField() {
        return usernameTextField;
    }
     PasswordField getPasswordTextField() {
        return passwordTextField;
    }
     Button getLoginButton() {
        return registerButton;
    }
     Button getContinueAsGuestButton() {
        return continueAsGuestButton;
    }
     Button getBackButton() {
        return backButton;
    }

     ButtonBase getRegisterButton() {
        return  registerButton;
    }
     PasswordField getConfirmPasswordTextField() {
        return confirmPasswordTextField;
    }

}
