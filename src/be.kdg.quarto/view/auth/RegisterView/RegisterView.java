package be.kdg.quarto.view.auth.RegisterView;

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

public class RegisterView extends BorderPane {

    private Label registerLabel;
    private TextField usernameTextField;
    private PasswordField passwordTextField;
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
        passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");
        usernameTextField.setFont(FontHelper.getMediumFont());
        passwordTextField.setFont(FontHelper.getMediumFont());

        registerButton = CreateHelper.createButton("Register", new String[] {"green-button", "default-button"});
        continueAsGuestButton = CreateHelper.createButton("Continue as Guest", new String[] {"orange-button", "default-button"});
        backButton = CreateHelper.createButton("Back", new String[] {"red-button", "default-button"});

        this.setTop(registerLabel);
    }

    private void layoutNodes() {
        VBox vbox = CreateHelper.createVBox("login-main-vbox");
        vbox.getChildren().addAll(registerLabel, usernameTextField, passwordTextField, registerButton, continueAsGuestButton, backButton);
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
        return registerButton;
    }
    public Button getContinueAsGuestButton() {
        return continueAsGuestButton;
    }
    public Button getBackButton() {
        return backButton;
    }
}
