package be.kdg.quarto.view.PieceView;

import be.kdg.quarto.model.Piece;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class PieceView extends StackPane{
    private ImageView piece;
    private Rectangle pieceRect;

    public PieceView(){
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        pieceRect = new Rectangle(20,20);
        piece = new ImageView();
    }

    private void layoutNodes() {
        getChildren().addAll(pieceRect, piece);
    }
}
