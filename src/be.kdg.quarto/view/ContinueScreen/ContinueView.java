package be.kdg.quarto.view.ContinueScreen;

import be.kdg.quarto.helpers.CreateHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class ContinueView extends BorderPane {

    private Label title;
    private VBox gameListContainer;
    private List<Button> continueButtons;
    private Button backButton;

    public ContinueView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        title = CreateHelper.createLabel("Continue Playing", "title");
        gameListContainer = new VBox();
        continueButtons = new ArrayList<>();

        backButton = CreateHelper.createButton("Back", new String[]{"default-button", "red-button"});
    }

    private void layoutNodes() {
        ScrollPane scrollPane = new ScrollPane(gameListContainer);
        scrollPane.setFitToWidth(true);

        setTop(title);
        setCenter(scrollPane);
        setBottom(backButton);

        setAlignment(title, Pos.TOP_CENTER);
        setAlignment(backButton, Pos.TOP_CENTER);
        setPadding(new Insets(10));
        setMargin(scrollPane, new Insets(10));
    }

    public void addGameRow(int index, String difficulty, String time) {
        Label indexLabel = CreateHelper.createLabel(Integer.toString(index), "info");
        Label difficultyLabel = CreateHelper.createLabel("Difficulty: " + difficulty, "info");
        Label durationLabel = CreateHelper.createLabel("Duration: " + time, "info");
        Button continueButton = CreateHelper.createButton("Continue", new String[]{"default-button", "blue-button"});

        GridPane row = new GridPane();
        row.setHgap(10);
        row.setPadding(new Insets(10));
        row.setPrefWidth(Double.MAX_VALUE);
        row.getStyleClass().add("row-style");

        // Column constraints: 10%, 35%, 35%, 20%
        double[] percentages = {10, 35, 35, 20};
        for (double percent : percentages) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(percent);
            col.setFillWidth(true);
            row.getColumnConstraints().add(col);
        }

        row.add(indexLabel, 0, 0);
        row.add(difficultyLabel, 1, 0);
        row.add(durationLabel, 2, 0);
        row.add(continueButton, 3, 0);

        GridPane.setFillWidth(continueButton, true);
        GridPane.setHalignment(continueButton, javafx.geometry.HPos.RIGHT);

        gameListContainer.getChildren().add(row);
        continueButtons.add(continueButton);
    }

    List<Button> getContinueButtons() {
        return continueButtons;
    }

    Button getBackButton() {
        return backButton;
    }
}
