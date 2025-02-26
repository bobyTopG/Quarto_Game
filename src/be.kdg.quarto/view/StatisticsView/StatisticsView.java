package be.kdg.quarto.view.StatisticsView;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatisticsView extends BorderPane {
    // Private attributes for nodes
    private ToggleButton playerBtn, aiBtn;
    private ToggleGroup group;
    private Label infoLabel;
    private HBox btnBox;
    private VBox vBox;

    private Label titleLabel;
    private Button closeBtn;

    // Constructor
    public StatisticsView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        playerBtn = new ToggleButton("Player");
        aiBtn = new ToggleButton("AI");
        group = new ToggleGroup();
        infoLabel = new Label("Player info");
        btnBox = new HBox();
        vBox = new VBox();

        titleLabel = new Label("[someone] Won!");
        closeBtn = new Button("Close");

        playerBtn.setToggleGroup(group);
        aiBtn.setToggleGroup(group);
        btnBox.getChildren().addAll(playerBtn, aiBtn);
        btnBox.setSpacing(10);
        vBox.getChildren().addAll(btnBox, infoLabel);
    }

    private void layoutNodes() {
        setPadding(new Insets(10, 10, 10, 10));
        setTop(titleLabel);
        setCenter(vBox);
        setBottom(closeBtn);


        setAlignment(btnBox, Pos.CENTER);
        playerBtn.setPrefWidth(200);
        playerBtn.setStyle(getOnStyle());

        aiBtn.setPrefWidth(200);
        aiBtn.setStyle(getOffStyle());

        setAlignment(closeBtn, Pos.CENTER);
        closeBtn.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        closeBtn.setStyle(
                "-fx-background-color: #f62626; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #070303; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20; " +
                        "-fx-cursor: hand;");
    }

    // Package private getters
    public ToggleButton getPlayerBtn() {
        return playerBtn;
    }

    public ToggleButton getAiBtn() {
        return aiBtn;
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public Button getCloseBtn() {
        return closeBtn;
    }

    public String getOnStyle() {
        return "-fx-background-color: #14526a; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 20; " +
                "-fx-border-color: #070303; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 20; " +
                "-fx-cursor: hand;";
    }

    public String getOffStyle() {
        return "-fx-background-color: #2dbdfa; " +
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
