package be.kdg.quarto.view.StatisticsView;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StatisticsView extends BorderPane {
    private Button playerBtn;
    private Button aiBtn;
    private Label infoLabel;
    private Button closeBtn;
    private Button backBtn;
    private Button nextBtn;

    // Constructor
    public StatisticsView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        Label titleLabel = new Label("[someone] Won!");

        playerBtn = new Button("Player");
        aiBtn = new Button("AI");

        HBox tBtnBox = new HBox();
        tBtnBox.getChildren().addAll(playerBtn, aiBtn);
        tBtnBox.setSpacing(10);
        VBox topBox = new VBox();
        topBox.getChildren().addAll(titleLabel, tBtnBox);

        infoLabel = new Label();

        closeBtn = new Button("Close");
        backBtn = new Button("Back");
        nextBtn = new Button("Next");

        HBox bBtnBox = new HBox();
        bBtnBox.getChildren().addAll(backBtn, nextBtn);
        bBtnBox.setSpacing(10);
        BorderPane bottomLayout = new BorderPane();
        bottomLayout.setLeft(closeBtn);
        bottomLayout.setRight(bBtnBox);

        setTop(topBox);
        setCenter(infoLabel);
        setBottom(bottomLayout);
    }

    private void layoutNodes() {
        setPadding(new Insets(10, 10, 10, 10));

        playerBtn.setPrefWidth(200);
        playerBtn.setStyle(getBtnStyle("#2dbdfa"));
        aiBtn.setPrefWidth(200);
        aiBtn.setStyle(getBtnStyle("#2dbdfa"));

        closeBtn.setStyle(getBtnStyle("#f62626"));
        backBtn.setStyle(getBtnStyle("#d2941f"));
        nextBtn.setStyle(getBtnStyle("#28a745"));
    }

    // Package private getters
    public Button getPlayerBtn() {
        return playerBtn;
    }

    public Button getAiBtn() {
        return aiBtn;
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public Button getCloseBtn() {
        return closeBtn;
    }

    public Button getBackBtn() {
        return backBtn;
    }

    public Button getNextBtn() {
        return nextBtn;
    }

    public String getBtnStyle(String hex) {
        return "-fx-background-color: " + hex + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 20; " +
                "-fx-border-color: #070303; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 20; " +
                "-fx-cursor: hand;";
    }
}
