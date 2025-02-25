package be.kdg.quarto.view.StatisticsView;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        infoLabel = new Label();
        btnBox = new HBox();
        vBox = new VBox();

        titleLabel = new Label();
        closeBtn = new Button("Close");

        playerBtn.setToggleGroup(group);
        aiBtn.setToggleGroup(group);
        btnBox.getChildren().addAll(playerBtn, aiBtn);
        vBox.getChildren().addAll(btnBox, infoLabel);
    }

    private void layoutNodes() {
        setTop(titleLabel);
        setCenter(vBox);
        setBottom(closeBtn);
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
}
