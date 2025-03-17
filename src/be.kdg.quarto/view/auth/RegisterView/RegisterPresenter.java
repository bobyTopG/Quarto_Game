package be.kdg.quarto.view.auth.RegisterView;

import be.kdg.quarto.view.ChooseAIView.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIView.ChooseAIView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import be.kdg.quarto.view.UiSettings;
import javafx.application.Application;
import javafx.stage.Stage;

public class RegisterPresenter {

    private RegisterView view;
    public RegisterPresenter(RegisterView view) {
        this.view = view;
        updateView();
        addEventHandlers();
    }

    UiSettings uiSettings = new UiSettings();
    private void addEventHandlers() {
        view.getBackButton().setOnAction(event -> {
            StartView startView = new StartView(uiSettings);
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });

        view.getContinueAsGuestButton().setOnAction(event -> {
            ChooseAIView chooseAIView = new ChooseAIView();
            view.getScene().setRoot(chooseAIView);
            new ChooseAIPresenter(chooseAIView);
        });
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}
