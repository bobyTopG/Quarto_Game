package be.kdg.quarto.view.StatisticsView;

import be.kdg.quarto.helpers.CreateHelper;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StatisticsView extends BorderPane {
    private Label titleLabel;
    private Button playerBtn;
    private Button aiBtn;
    private Label infoLabel;
    private VBox infoGroup;
    private Button closeBtn;
    private Button backBtn;
    private Button nextBtn;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series1;
    private XYChart.Series<Number, Number> series2;

    // Constructor
    public StatisticsView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        titleLabel = CreateHelper.createLabel("", "win-title");

        playerBtn = CreateHelper.createButton("Player", new String[]{"default-button", "blue-button"});
        playerBtn.setId("player-statistics-button");
        aiBtn = CreateHelper.createButton("AI", new String[]{"default-button", "blue-button"});
        aiBtn.setId("ai-statistics-button");

        infoLabel = CreateHelper.createLabel("", "info-label");

        HBox centerBtnBox = new HBox();
        centerBtnBox.getChildren().addAll(playerBtn, aiBtn);
        infoGroup = new VBox();
        infoGroup.getChildren().addAll(centerBtnBox, infoLabel);

        // line chart
        lineChart = new LineChart<>(new NumberAxis(), new NumberAxis());
        lineChart.setTitle("Performance Statistics");
        lineChart.setAnimated(false);
        series1 = new XYChart.Series<>();
        series1.setName("Player 1");
        series2 = new XYChart.Series<>();
        series2.setName("Player 2");

        closeBtn = CreateHelper.createButton("Close", new String[]{"default-button", "red-button"});
        backBtn = CreateHelper.createButton("Back", new String[]{"default-button", "orange-button"});
        nextBtn = CreateHelper.createButton("Next", new String[]{"default-button", "green-button"});

        HBox bottomBtnBox = new HBox();
        bottomBtnBox.getChildren().addAll(backBtn, nextBtn);
        bottomBtnBox.setSpacing(10);
        BorderPane bottomLayout = new BorderPane();
        bottomLayout.setLeft(closeBtn);
        bottomLayout.setRight(bottomBtnBox);

        setTop(titleLabel);
        setBottom(bottomLayout);
    }

    private void layoutNodes() {
        setPadding(new Insets(10, 10, 10, 10));

        playerBtn.setPrefWidth(200);
        aiBtn.setPrefWidth(200);
    }

    // Package private getters
    Label getTitleLabel() {
        return titleLabel;
    }

    VBox getInfoGroup() {
        return infoGroup;
    }

    LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

    XYChart.Series<Number, Number> getSeries1() {
        return series1;
    }

    XYChart.Series<Number, Number> getSeries2() {
        return series2;
    }

    Button getPlayerBtn() {
        return playerBtn;
    }

    Button getAiBtn() {
        return aiBtn;
    }

    Label getInfoLabel() {
        return infoLabel;
    }

    Button getCloseBtn() {
        return closeBtn;
    }

    Button getBackBtn() {
        return backBtn;
    }

    Button getNextBtn() {
        return nextBtn;
    }


}
