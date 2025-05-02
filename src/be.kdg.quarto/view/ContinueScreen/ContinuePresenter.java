package be.kdg.quarto.view.ContinueScreen;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.DbConnection;
import be.kdg.quarto.helpers.ErrorHelper;
import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.scene.control.Button;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ContinuePresenter {

    private ContinueView view;
    private Map<Integer, String[]> sessions = new LinkedHashMap<>();

    public ContinuePresenter(ContinueView view) {
        this.view = view;
        updateView();
        addEventHandlers();
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");

        loadGameSessions();

        int count = 1;
        for (Map.Entry<Integer, String[]> entry : sessions.entrySet()) {
            view.addGameRow(count, entry.getValue()[0], entry.getValue()[1]);
            count++;
        }

    }

    private void loadGameSessions() {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.loadUnfinishedSessions())) {
            ps.setInt(1, AuthHelper.getLoggedInPlayer().getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int sessionId = rs.getInt("game_session_id");
                int level = rs.getInt("player_id2") - 1;
                String difficulty = (level >= 0 && level < AiLevel.values().length)
                        ? AiLevel.values()[level].toString()
                        : "FRIENDLY";

                //if gameSession does not have any pause periods with null end_time
                // that means that the game was suddenly closed and not saved properly
                if(rs.getBoolean("is_corrupted"))
                    sessions.put(sessionId, new String[]{difficulty, rs.getString("formatted_duration")});
            }
        } catch (SQLException e) {
            ErrorHelper.showDBError(e);
        }
    }

    private void addEventHandlers() {
        int index = 0;
        for (Map.Entry<Integer, String[]> entry : sessions.entrySet()) {
            Button button = view.getContinueButtons().get(index);
            int sessionId = entry.getKey();

            button.setOnAction(event -> onGameSelected(sessionId));
            index++;
        }

        view.getBackButton().setOnAction(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });
    }

    private void onGameSelected(int sessionId) {
        GameSession model = null;
        try {
            model = new GameSession(sessionId);

        GameView gameView = new GameView(ImageHelper.getPlayerImage(), ImageHelper.getOpponentImage(model.getOpponent()), model.getOpponent().getName());


        view.getScene().setRoot(gameView);
        new GamePresenter(model, gameView);
        } catch (SQLException e) {
            CreateHelper.showAlert("Database Error", e.getMessage(), true);
        } catch (Exception e) {
            CreateHelper.showAlert("Something went wrong", e.getMessage(), true);
        }
    }
}
