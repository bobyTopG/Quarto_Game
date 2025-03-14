package be.kdg.quarto.view.GameScreen.SettingsScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;

public class SettingsPresenter {
    private Game model;
    private GamePresenter gamePresenter;
    private SettingsView view;

    public SettingsPresenter(GamePresenter game, SettingsView view, Game model) {
        this.gamePresenter = game;
        this.view = view;
        this.model = model;

        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getRestartButton().setOnAction(event -> {
            model.restartGame();
            closeSettings();
        });

        view.getResumeButton().setOnAction(event -> {
           closeSettings();
        });

        view.getExitButton().setOnAction(event -> {
           StartView startView= new StartView();
           view.getScene().setRoot(startView);
           new StartPresenter(model, startView);
        });
    }

    private void updateView() {
    }

    private void closeSettings(){
        gamePresenter.getView().getOverlayContainer().setVisible(false);
    }
}
