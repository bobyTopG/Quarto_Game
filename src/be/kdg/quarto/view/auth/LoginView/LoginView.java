package be.kdg.quarto.view.auth.LoginView;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView extends BorderPane {

    private Label loginLabel;
    private TextField usernameTextField;
    private PasswordField passwordTextField;
    private Button loginButton;

    public LoginView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        loginLabel = new Label("Login");
        usernameTextField = new TextField();
        passwordTextField = new PasswordField();
        loginButton = new Button("Login");
    }

    private void layoutNodes() {
        loginLabel.setStyle("-fx-font-size: 18px;");
        usernameTextField.setStyle("-fx-font-size: 18px;");
        passwordTextField.setStyle("-fx-font-size: 18px;");
        loginButton.setStyle("-fx-font-size: 18px;");
    }
}
