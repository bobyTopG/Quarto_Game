package be.kdg.quarto.view.WinScreen;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.FontHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class WinView extends StackPane {

    private Button exitButton;
    private Button restartButton;
    Label winLabel;

    public WinView() {
        initialiseNodes();
        layoutNodes();

    }

    private void initialiseNodes() {
        exitButton = CreateHelper.createButton("Menu", new String[]{"orange-button", "default-button","win-button"});
        restartButton = CreateHelper.createButton("Restart", new String[]{"green-button", "default-button","win-button"});
        winLabel = CreateHelper.createLabel("Opponent Wins!","win-label");
        this.getStyleClass().add("win-screen");

    }



    private void layoutNodes() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30); // Add spacing between elements

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll(exitButton, restartButton);
        hbox.setAlignment(Pos.CENTER); // Center buttons horizontally

        // Add winLabel and hbox to the VBox
        vbox.getChildren().addAll(winLabel, hbox);

        // Only add the VBox to the StackPane
        this.getChildren().add(vbox);
    }




    //we use public getters in order for the logic to work(to use the functions and model from another presenter )
    public Button getExitButton() {
        return exitButton;
    }

    public Button getRestartButton() {
        return restartButton;
    }
    public void setWinner(String name, boolean isOpponent) {
        if(name != null){
            winLabel.setStyle(!isOpponent ? "-fx-background-color: #29ABE2" : "-fx-background-color: rgb(218,66,66)");
            String text = isOpponent ? (name + " Wins!") : "You Win!";
            winLabel.setText(text);
        }else{
            winLabel.setStyle("-fx-background-color: #e6621d");
            winLabel.setText("It is a Draw!");
        }

    }

}
