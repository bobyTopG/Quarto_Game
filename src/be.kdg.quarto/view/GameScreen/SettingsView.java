package be.kdg.quarto.view.GameScreen;

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
    private Button saveButton;
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
        saveButton = new Button("Save");
        exitButton = new Button("Exit");
        restartButton = new Button("Restart");
        resumeButton = new Button("Resume");
        pausedLabel =  CreateHelper.createLabel("PAUSE","paused-label");



        saveButton = CreateHelper.createButton("Save Game",  new String[]{"green-button", "default-button"});
        exitButton = CreateHelper.createButton("Quit", new String[]{"red-button", "default-button"});
        restartButton = CreateHelper.createButton("Restart", new String[]{"red-button", "default-button"});
        resumeButton = CreateHelper.createButton("Resume", new String[]{"blue-button", "default-button"});
        this.getStyleClass().add("settings-screen");


    }



    private void layoutNodes() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll( exitButton, saveButton ,resumeButton, restartButton);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        pausedLabel.setFont(FontHelper.getExtraLargeFont());

        vbox.getChildren().addAll(pausedLabel,hbox);
        this.getChildren().add(vbox);

    }


    public Button getExitButton() {
        return exitButton;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

}
