package be.kdg.quarto.view.StatisticsScreen;

import be.kdg.quarto.model.Statistics;
import javafx.scene.chart.XYChart;

public class StatisticsPresenter {
    private final StatisticsView view;
    private final Statistics stats;

    public StatisticsPresenter(StatisticsView view, Statistics stats) {
        this.view = view;
        this.stats = stats;
        addEventHandlers();
        updateView();

        view.getPlayerBtn().fire();
        view.getBackBtn().fire();
        view.getTitleLabel().setText(stats.getWinner() == null ? "Draw!" : stats.getWinner() + " Won!");
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        view.getPlayerBtn().setText(stats.getPlayer1());
        view.getAiBtn().setText(stats.getPlayer2());
        view.getSeries1().setName(stats.getPlayer1());
        view.getSeries2().setName(stats.getPlayer2());
    }

    private void addEventHandlers() {
        view.getPlayerBtn().setOnAction(event -> {
            stats.setPlayerIdTemp(stats.getPlayerId1());
            view.getInfoLabel().setText(stats.loadPartialStatistics());
            view.getPlayerBtn().setDisable(true);
            view.getAiBtn().setDisable(false);
        });

        view.getAiBtn().setOnAction(event -> {
            stats.setPlayerIdTemp(stats.getPlayerId2());
            view.getInfoLabel().setText(stats.loadPartialStatistics());
            view.getPlayerBtn().setDisable(false);
            view.getAiBtn().setDisable(true);
        });

        view.getBackBtn().setOnAction(event -> {
            view.setCenter(view.getInfoGroup());

            view.getBackBtn().setDisable(true);
            view.getNextBtn().setDisable(false);
        });

        view.getNextBtn().setOnAction(event -> {
            view.setCenter(view.getLineChart());
            for (Statistics.Move move : stats.loadStatistics()) {
                if (move.getPlayerId() == stats.getPlayerId1()) {
                    view.getSeries1().getData().add(new XYChart.Data<>(move.getMoveNumber(), move.getTime()));
                } else {
                    view.getSeries2().getData().add(new XYChart.Data<>(move.getMoveNumber(), move.getTime()));
                }
            }
            view.getLineChart().getData().clear();
            view.getLineChart().getData().addAll(view.getSeries1(), view.getSeries2());

            view.getBackBtn().setDisable(false);
            view.getNextBtn().setDisable(true);
        });

        view.getCloseBtn().setOnAction(event -> {

        });
    }

}