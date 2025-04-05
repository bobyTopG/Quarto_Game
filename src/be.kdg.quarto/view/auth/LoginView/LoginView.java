package be.kdg.quarto.view.auth.LoginView;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import be.kdg.quarto.helpers.FontHelper;
import be.kdg.quarto.helpers.CreateHelper;
import javafx.stage.Stage;

public class LoginView extends BorderPane {

    private Label loginLabel;
    private TextField usernameTextField;
    private PasswordField passwordTextField;
    private Button loginButton;
    private Button backButton;

    private HBox mainHBox;


    public LoginView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        loginLabel = CreateHelper.createLabel("Login", "main-title");

        usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        usernameTextField.getStyleClass().addAll("default-button", "white-text-field");

        passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");
        passwordTextField.getStyleClass().addAll("default-button", "white-text-field");

        loginButton = CreateHelper.createButton("Login", new String[] {"green-button", "default-button"});
        backButton = CreateHelper.createButton("Back", new String[] {"red-button", "default-button"});
        this.setTop(loginLabel);
    }

    private void layoutNodes() {
        HBox buttonHBox = CreateHelper.createHBox("login-button-hbox");
        buttonHBox.getChildren().addAll(loginButton, backButton);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(10);

        VBox vbox = CreateHelper.createVBox("login-main-vbox");
        vbox.getChildren().addAll(loginLabel, usernameTextField, passwordTextField, buttonHBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

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
        return loginButton;
    }
     Button getBackButton() {
        return backButton;
    }
}
