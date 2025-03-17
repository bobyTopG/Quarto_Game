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
        loginLabel = CreateHelper.createLabel("Login", "main-login");
        loginLabel.setFont(FontHelper.getLargeFont());

        usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");
        usernameTextField.setFont(FontHelper.getMediumFont());
        passwordTextField.setFont(FontHelper.getMediumFont());

        loginButton = CreateHelper.createButton("Login", new String[] {"green-button", "default-button"});
        backButton = CreateHelper.createButton("Back", new String[] {"red-button", "default-button"});

        this.setTop(loginLabel);
    }

    private void layoutNodes() {
        VBox vbox = CreateHelper.createVBox("login-main-vbox");
        vbox.getChildren().addAll(loginLabel, usernameTextField, passwordTextField, loginButton, backButton);
        vbox.setAlignment(Pos.CENTER);


        mainHBox = CreateHelper.createHBox("login-main-hbox");
        mainHBox.getChildren().addAll(vbox);
        mainHBox.setAlignment(Pos.CENTER);
        this.setCenter(mainHBox);

    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }
    public PasswordField getPasswordTextField() {
        return passwordTextField;
    }
    public Button getLoginButton() {
        return loginButton;
    }
    public Button getBackButton() {
        return backButton;
    }
}
