package be.kdg.quarto.view.auth.LoginView;

import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import be.kdg.quarto.view.UiSettings;
import javafx.application.Application;
import javafx.stage.Stage;

public class LoginPresenter {

    private LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
        updateView();
        addEventHandlers();
    }

    UiSettings uiSettings = new UiSettings();
    private void addEventHandlers() {
        view.getBackButton().setOnAction(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}
