package be.kdg.quarto.view.PieceView;

import be.kdg.quarto.model.Piece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class PieceView extends StackPane {
    private Rectangle pieceRect;
    private Piece piece;


    public PieceView() {
        initialiseNodes();
        layoutNodes();
    }

    public Rectangle getPieceRect() {
        return pieceRect;
    }

    public void setPieceRect(Rectangle pieceRect) {
        this.pieceRect = pieceRect;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    private void initialiseNodes() {
        //imageView = new ImageView();
        pieceRect = new Rectangle(45, 45);

    }


    private void layoutNodes() {

        pieceRect.setFill(Color.WHITE);
        pieceRect.setArcHeight(20);
        pieceRect.setArcWidth(20);
        pieceRect.setStroke(Color.BLACK);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pieceRect);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll( stackPane);
        getChildren().add(hbox);
    }
}
