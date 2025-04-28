package be.kdg.quarto.view.ContinueScreen;

import be.kdg.quarto.helpers.CreateHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        title = CreateHelper.createLabel("Continue", "title");
        gameListContainer = new VBox(10);
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
        Label label = CreateHelper.createLabel(String.format("%d.   Difficulty: %-20s Duration: %-20s", index, difficulty, time), "info");
        Button continueButton = CreateHelper.createButton("Continue", new String[]{"default-button", "blue-button"});

        // Creates a row for each game
        HBox row = new HBox(50, label, continueButton);
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
