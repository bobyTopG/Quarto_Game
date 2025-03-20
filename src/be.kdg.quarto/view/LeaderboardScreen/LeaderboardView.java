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
        Label titleLabel = CreateHelper.createLabel("Leaderboard", "title");

        backBtn = CreateHelper.createButton("Back", new String[]{"default-button", "red-button"});

        setTop(titleLabel);
        setAlignment(titleLabel, Pos.CENTER);
        setMargin(titleLabel, new Insets(0, 0, 5, 0));
        // center is set in the presenter
        setBottom(backBtn);
    }

    private void layoutNodes() {
        setPadding(new Insets(10, 5, 10, 5));
        setMargin(backBtn, new Insets(5, 0, 0, 0));
        setAlignment(backBtn, Pos.CENTER);
    }

    // Package private getters
    Button getBackBtn() {
        return backBtn;
    }
}
