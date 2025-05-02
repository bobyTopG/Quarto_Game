package be.kdg.quarto.view.SettingsScreen;

import be.kdg.quarto.helpers.ErrorHelper;
import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.PausePeriod;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;

import java.sql.SQLException;

public class SettingsPresenter {
    private final SettingsView view;
    private final GameSession model;
    private final GamePresenter gamePresenter;

    public SettingsPresenter(SettingsView view, GameSession model, GamePresenter gamePresenter) {
        this.view = view;
        this.model = model;
        this.gamePresenter = gamePresenter;

        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getRestartButton().setOnAction(event -> {
            try {
                restartGame();
            } catch (SQLException e) {
                ErrorHelper.showDBError(e);
            }
           this.gamePresenter.closeSettings();
        });

        view.getResumeButton().setOnAction(event -> {
            resumeGame();
            this.gamePresenter.closeSettings();
        });

        view.getExitButton().setOnAction(event -> exitToMainMenu());
    }

    private void restartGame() throws SQLException {
        // Create a new Game instance with the same players from the current game
        GameSession newGameSession = new GameSession(
                model.getPlayer(),
                model.getOpponent(),
                model.getOpponent(),
                model.isOnline
        );

        // Create a new view
        GameView newGameView = new GameView(
                ImageHelper.getPlayerImage(),
                ImageHelper.getOpponentImage(model.getOpponent()),
                model.getOpponent().getName()
        );

        // Replace the scene root
        gamePresenter.getView().getScene().setRoot(newGameView);

        // Create a new presenter
        new GamePresenter(newGameSession, newGameView);
    }

    private void resumeGame() {
        model.getGameTimer().resumeGame();
        gamePresenter.resumeTimer();
    }

    private void exitToMainMenu() {
        if (model.isOnline) {
            model.getGame().getCurrentMove().getPausePeriods().add(
                    new PausePeriod(model.getGameTimer().getCurrentPauseStart(), null)
            );

            try {
                model.saveMoveToDb(model.getGame().getCurrentMove());
            } catch (SQLException e) {
                ErrorHelper.showDBError(e);
            }
        }

        StartView startView = new StartView();
        gamePresenter.getView().getScene().setRoot(startView);
        new StartPresenter(startView);
    }

}