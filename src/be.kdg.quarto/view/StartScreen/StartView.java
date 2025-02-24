package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.BoardView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StartView extends BorderPane {

    private Label label;
    private Button newGame, continueButton, leaderBoard, statistics, quit;
    private BoardView board;

    public StartView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        board = new BoardView();
        label = new Label("Quarto!");
        newGame = new Button("New Game");
        continueButton = new Button("Continue");
        leaderBoard = new Button("Leaderboard");
        statistics = new Button("Statistics");
        quit = new Button("Quit");
    }

    public BoardView getBoard() {
        return board;
    }

    private void layoutNodes() {
        label.setFont(Font.font("Sigmar", FontWeight.BOLD, 40));

        newGame.setStyle(
                "-fx-background-color: #28a745; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );

        continueButton.setStyle(
                "-fx-background-color: #d2941f; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #070501; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );

        leaderBoard.setStyle(
                "-fx-background-color: #2dbdfa; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );

        statistics.setStyle(
                "-fx-background-color: #2dbdfa; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );

        quit.setStyle(
                "-fx-background-color: #f62626; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #070303; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );


        continueButton.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        leaderBoard.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        statistics.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        quit.setFont(Font.font("Arial", FontWeight.BOLD, 24));


        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(10, 10, 20, 20));
        vbox.getChildren().addAll(newGame, continueButton, leaderBoard, statistics, quit);
        vbox.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane();
        board.disableBoard();
        stackPane.getChildren().addAll(board);
        HBox hBox = new HBox(250, vbox, stackPane);
        setAlignment(label, Pos.CENTER);
        setTop(label);
        setCenter(hBox);
    }

    public Button getContinueButton() {
        return continueButton;
    }

    public Label getLabel() {
        return label;
    }

    public Button getLeaderBoard() {
        return leaderBoard;
    }

    public Button getNewGame() {
        return newGame;
    }

    public Button getQuit() {
        return quit;
    }

    public Button getStatistics() {
        return statistics;
    }
}
