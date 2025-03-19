package be.kdg.quarto.view.LeaderboardScreen;

import be.kdg.quarto.model.Leaderboard;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import be.kdg.quarto.view.UiSettings;

public class LeaderboardPresenter {
    private final LeaderboardView view;
    private final Leaderboard leaderboard;

    public LeaderboardPresenter(LeaderboardView view, Leaderboard leaderboard) {
        this.view = view;
        this.leaderboard = leaderboard;
        addEventHandlers();
        updateView();
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        view.getTable().setItems(leaderboard.loadLeaderboard());
    }

    private void addEventHandlers() {
        view.getBackBtn().setOnAction(event -> {
            StartView startView = new StartView(new UiSettings());
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });
    }
}
