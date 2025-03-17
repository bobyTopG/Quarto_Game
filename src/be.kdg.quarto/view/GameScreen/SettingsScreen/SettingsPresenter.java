package be.kdg.quarto.view.GameScreen.SettingsScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.PieceView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;

public class SettingsPresenter {
    private GamePresenter gamePresenter;
    private SettingsView view;
    private Game model;

    public SettingsPresenter(GamePresenter game, SettingsView view, Game model) {
        this.gamePresenter = game;
        this.view = view;
        this.model = model;

        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getRestartButton().setOnAction(event -> {
            //todo: make this to work
            model.restartGame();
            gamePresenter.getView().getBoard().clearBoard(); // Clear the existing BoardView
            gamePresenter.updateView();
            closeSettings();
        });

        view.getResumeButton().setOnAction(event -> {
            closeSettings();
        });

        view.getExitButton().setOnAction(event -> {
            model.restartGame();
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });
    }

    private void updateView() {
    }

    private void closeSettings() {
        gamePresenter.getView().getOverlayContainer().setVisible(false);
    }
}
