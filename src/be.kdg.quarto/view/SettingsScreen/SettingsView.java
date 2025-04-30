package be.kdg.quarto.view.SettingsScreen;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.FontHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SettingsView extends StackPane {
    private Button exitButton;
    private Button restartButton;
    private Button resumeButton;
    Label pausedLabel;

    public SettingsView() {
        this.setPrefSize(500, 300);
        this.setMaxSize(800, 300);

        initialiseNodes();
        layoutNodes();

    }

    private void initialiseNodes() {
        pausedLabel =  CreateHelper.createLabel("PAUSE","paused-label");
        exitButton = CreateHelper.createButton("Menu", new String[]{"red-button", "default-button"});
        restartButton = CreateHelper.createButton("Restart", new String[]{"blue-button", "default-button"});
        resumeButton = CreateHelper.createButton("Resume", new String[]{"green-button", "default-button"});
        this.getStyleClass().add("settings-screen");

    }



    private void layoutNodes() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll( exitButton ,resumeButton, restartButton);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        pausedLabel.setFont(FontHelper.getExtraLargeFont());

        vbox.getChildren().addAll(pausedLabel,hbox);
        this.getChildren().add(vbox);

    }


    //we use public getters in order for the logic to work(to use the functions and model from another presenter )
    // check in the GamePresenter how it is used
     public Button getRestartButton() {
        return restartButton;
    }
     public Button getResumeButton() {
        return resumeButton;
    }

    public Button getExitButton() {
        return exitButton;
    }



}
