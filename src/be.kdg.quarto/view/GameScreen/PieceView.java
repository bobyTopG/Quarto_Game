package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Piece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class PieceView extends StackPane {
    private Rectangle pieceRect;
    private ImageView pieceImage;

    public Rectangle getPieceRect() {
        return pieceRect;
    }

    public ImageView getPieceImage() {
        return pieceImage;
    }

    public void setPieceImage(ImageView pieceImage) {
        this.pieceImage = pieceImage;
    }
    public void clearPiece() {
        this.getPieceImage().setImage(null);
    }
    public PieceView() {
        initialiseNodes();
        layoutNodes();
    }


    private void initialiseNodes() {
        pieceImage = new ImageView();
        pieceRect = new Rectangle(45, 45);

    }


    private void layoutNodes() {
        pieceRect.setFill(Color.TRANSPARENT);
        pieceRect.setArcHeight(20);
        pieceRect.setArcWidth(20);
        pieceRect.setStroke(Color.BLACK);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pieceRect);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.getChildren().addAll(stackPane);
        pieceImage.setFitHeight(35);
        pieceImage.setFitWidth(35);
        getChildren().addAll(hbox,pieceImage);
    }
}
