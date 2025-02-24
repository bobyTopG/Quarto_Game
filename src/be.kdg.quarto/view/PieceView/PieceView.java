package be.kdg.quarto.view.PieceView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class PieceView extends GridPane {
    private Label label;
    private PieceView piece;
    private Rectangle pieceRect;

    public PieceView() {
        initialiseNodes();
        layoutNodes();
    }

    public PieceView getPiece() {
        return piece;
    }


    private void initialiseNodes() {
        label = new Label("Piece: ");
        pieceRect = new Rectangle(50, 50);
        piece = new PieceView();

    }

    public Rectangle getPieceRect() {
        return pieceRect;
    }

    private void layoutNodes() {

        pieceRect.setFill(Color.WHITE);
        pieceRect.setArcHeight(20);
        pieceRect.setArcWidth(20);
        pieceRect.setStroke(Color.BLACK);

        StackPane stackPane = new StackPane(pieceRect , piece);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll(label, stackPane);
        getChildren().add(hbox);
    }
}
