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

        view.getPlayerBtn().fire();
        view.getBackBtn().fire();
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }

    private void addEventHandlers() {
        view.getPlayerBtn().setOnAction(event -> {
            view.getInfoLabel().setText("Player");
            view.getPlayerBtn().setDisable(true);
            view.getAiBtn().setDisable(false);
        });

        view.getAiBtn().setOnAction(event -> {
            view.getInfoLabel().setText("AI");
            view.getPlayerBtn().setDisable(false);
            view.getAiBtn().setDisable(true);
        });

        view.getBackBtn().setOnAction(event -> {
            view.getBackBtn().setDisable(true);
            view.getNextBtn().setDisable(false);
        });

        view.getNextBtn().setOnAction(event -> {
            view.getBackBtn().setDisable(false);
            view.getNextBtn().setDisable(true);
        });

        view.getCloseBtn().setOnAction(event -> {
            ((Stage) view.getCloseBtn().getScene().getWindow()).close();
        });
    }

}