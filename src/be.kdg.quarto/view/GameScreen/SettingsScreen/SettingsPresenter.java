package be.kdg.quarto.view.GameScreen.SettingsScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
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
            // Close the settings overlay
            closeSettings();
            // Create a new Game instance with the same players from the current game
            Game newGame = new Game(model.getHuman(), model.getAI());
            // Create a new view (this will properly initialize all UI components)
            GameView newView = new GameView();

            // Replace the entire scene root with the new view
            view.getScene().setRoot(newView);

            // Create a new presenter with the new model and new view
            new GamePresenter(newGame, newView);
        });

        view.getResumeButton().setOnAction(event -> {
            closeSettings();
        });

        view.getExitButton().setOnAction(event -> {
            //model.restartGame();
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