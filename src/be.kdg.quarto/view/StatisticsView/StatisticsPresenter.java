package be.kdg.quarto.view.StatisticsView;

import be.kdg.quarto.model.Statistics;
import javafx.stage.Stage;

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
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }

    private void addEventHandlers() {
        view.getPlayerBtn().setOnAction(event -> {
            view.getInfoLabel().setText("Player info");
            view.getPlayerBtn().setStyle(view.getOnStyle());
            view.getAiBtn().setStyle(view.getOffStyle());
        });

        view.getAiBtn().setOnAction(event -> {
            view.getInfoLabel().setText("AI info");
            view.getAiBtn().setStyle(view.getOnStyle());
            view.getPlayerBtn().setStyle(view.getOffStyle());
        });

        view.getCloseBtn().setOnAction(event -> {
            ((Stage) view.getCloseBtn().getScene().getWindow()).close();
        });
    }

}