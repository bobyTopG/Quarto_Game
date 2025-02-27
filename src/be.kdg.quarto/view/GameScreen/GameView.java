package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.PieceView.PieceView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameView extends BorderPane {

    private PieceView piece;
    private SelectView selectView;
    private Label  turn, timer;
    private Button quarto, placePiece, choosePiece, settings;
    private BoardView board;


    public GameView() {
        initialiseNodes();
        layoutNodes();
    }

    public SelectView getSelectView() {
        return selectView;
    }

    private void initialiseNodes() {
        selectView = new SelectView();
        board = new BoardView();
        piece = new PieceView();
        turn = new Label("Your Turn");
        timer = new Label("00:00:00");
        quarto = new Button("Quarto");
        placePiece = new Button("Place Piece");
        choosePiece = new Button("Choose Piece");
        settings = new Button("Settings");
    }

    public BoardView getBoard() {
        return board;
    }

    public PieceView getPiece() {
        return piece;
    }

    public Label getTurn() {
        return turn;
    }

    private void layoutNodes() {

        quarto.setStyle(
                "-fx-background-color: #28a745; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );

        choosePiece.setStyle(
                "-fx-background-color: #d2941f; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #070501; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );

        placePiece.setStyle(
                "-fx-background-color: #2dbdfa; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );

        settings.setStyle(
                "-fx-background-color: #686a6a; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20;"
        );


        turn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        quarto.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        placePiece.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        choosePiece.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        settings.setFont(Font.font("Arial", FontWeight.BOLD, 24));


        VBox vbox = new VBox();
        vbox.setSpacing(25);
        vbox.setPadding(new Insets(10, 150, 10, 10));
        vbox.getChildren().addAll(quarto, placePiece, choosePiece, settings);
        vbox.setAlignment(Pos.CENTER);

        HBox hbox = new HBox();
        hbox.setSpacing(50);
        board.enableBoard();
        hbox.getChildren().addAll(vbox, board, selectView);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        StackPane pane = new StackPane();
        Rectangle r = new Rectangle(120,40);
        r.setArcWidth(20);
        r.setArcHeight(20);
        r.setFill(Color.web("#2dbdfa"));

        turn.setTextFill(Color.WHITE);
        pane.getChildren().addAll(r, turn);

        grid.add(pane, 0, 0);
        grid.add(piece, 3, 0);
        grid.add(timer, 30, 0);

        setTop(grid);
        setCenter(hbox);
    }
}
