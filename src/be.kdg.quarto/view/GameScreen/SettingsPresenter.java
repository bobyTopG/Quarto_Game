package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;

public class SettingsPresenter {
    private GamePresenter gamePresenter;
    private SettingsView view;
    private GameSession model;

    public SettingsPresenter(GamePresenter game, SettingsView view, GameSession model) {
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
            GameSession newGameSession = new GameSession(model.getPlayer(), model.getOpponent() , model.getOpponent());
            // Create a new view (this will properly initialize all UI components)
            GameView newView = new GameView(gamePresenter.getView().getPlayerImage(), gamePresenter.getView().getOpponentImage(), gamePresenter.getView().getOpponentName());

            // Replace the entire scene root with the new view
            view.getScene().setRoot(newView);

            // Create a new presenter with the new model and new view
            new GamePresenter(newGameSession, newView);
        });

        view.getResumeButton().setOnAction(event -> {
            model.getGameTimer().resumeGame();
            gamePresenter.resumeTimer();
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