package be.kdg.quarto.view.PieceView;

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

    public PieceView() {
        initialiseNodes();
        layoutNodes();
    }


    public Rectangle getPieceRect() {
        return pieceRect;
    }

    private void initialiseNodes() {
        pieceRect = new Rectangle(50, 50);

    }


    private void layoutNodes() {

        pieceRect.setFill(Color.WHITE);
        pieceRect.setArcHeight(20);
        pieceRect.setArcWidth(20);
        pieceRect.setStroke(Color.BLACK);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(pieceRect);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll( stackPane);
        getChildren().add(hbox);
    }
}
