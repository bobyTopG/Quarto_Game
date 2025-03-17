package be.kdg.quarto.view.GameScreen.SettingsScreen;

import be.kdg.quarto.helpers.CreateHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SettingsView extends StackPane {
    private Button saveButton;
    private Button exitButton;
    private Button restartButton;
    private Button resumeButton;


    public SettingsView() {
        this.setStyle("-fx-background-color: rgba(0,0,0,0.2); -fx-background-radius: 20px;");
        this.setPrefSize(500, 300);
        this.setMaxSize(500, 300);

        initialiseNodes();
        layoutNodes();

    }

    private void initialiseNodes() {
        saveButton = new Button("Save");
        exitButton = new Button("Exit");
        restartButton = new Button("Restart");
        resumeButton = new Button("Resume");


        saveButton = CreateHelper.createButton("Save Game",  new String[]{"green-button", "default-button"});
        exitButton = CreateHelper.createButton("Quit", new String[]{"red-button", "default-button"});
        restartButton = CreateHelper.createButton("Restart", new String[]{"red-button", "default-button"});
        resumeButton = CreateHelper.createButton("Resume", new String[]{"blue-button", "default-button"});

    }



    private void layoutNodes() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll( exitButton, saveButton ,resumeButton, restartButton);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        this.getChildren().add(hbox);
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
