package be.kdg.quarto.view.LeaderboardScreen;

import be.kdg.quarto.helpers.CreateHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class LeaderboardView extends BorderPane {

    private Button backBtn;


    // Constructor
    public LeaderboardView() {
        initializeNodes();
        layoutNodes();

    }

    private void initializeNodes() {
        Label title = CreateHelper.createLabel("Leaderboard", "title");

        backBtn = CreateHelper.createButton("Back", new String[]{"default-button", "red-button"});

        setTop(title);
        setAlignment(title, Pos.CENTER);
        // center is set in the presenter
        setBottom(backBtn);
    }

    private void layoutNodes() {
        setPadding(new Insets(10, 10, 10, 10));
        setMargin(backBtn, new Insets(10, 0, 0, 0));
        setAlignment(backBtn, Pos.CENTER);
    }

    // Package private getters
    Button getBackBtn() {
        return backBtn;
    }
}
