package be.kdg.quarto.view.WinScreen;

import be.kdg.quarto.helpers.ErrorHelper;
import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;

import java.sql.SQLException;

/**
 * Presenter for View that should appear after "Game Over" when Offline
 */
public class WinPresenter {
    private final WinView view;
    private final GameSession model;
    private final GamePresenter gamePresenter;

    public WinPresenter(WinView view, GameSession model, GamePresenter gamePresenter) {
        this.view = view;
        this.model = model;
        this.gamePresenter = gamePresenter;

        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getExitButton().setOnAction(event -> exitToMainMenu());

        view.getRestartButton().setOnAction(event -> {
            try {
                restartGame();
            } catch (SQLException e) {
                ErrorHelper.showDBError(e);
            }
            gamePresenter.closeSettings();
        });
    }


    /**
     *
     *  Deletes the old Game, starts a new Game
     */
    private void restartGame() throws SQLException {

        if(model.isOnline)
            model.deleteCurrentGameSessionFromDb();

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

    /**
     * Redirects to startView
     */
    private void exitToMainMenu() {
        StartView startView = new StartView();
        view.getScene().setRoot(startView);
        new StartPresenter(startView);
    }

    public void setWinner(String name, boolean isOpponent) {
        view.setWinner(name, isOpponent);
    }
}