package be.kdg.quarto.view.StatisticsView;

import be.kdg.quarto.model.Statistics;

public class StatisticsPresenter {
    private final StatisticsView view;
    private final Statistics stats;

    public StatisticsPresenter(StatisticsView view, Statistics stats) {
        this.view = view;
        this.stats = stats;
        addEventHandlers();
        updateView();
    }

    private void updateView() {
    }

    private void addEventHandlers() {
        view.getPlayerBtn().setOnAction(event -> {
            view.getInfoLabel().setText("Player info");
        });
        view.getAiBtn().setOnAction(event -> {
            view.getInfoLabel().setText("AI info");
        });
    }

}