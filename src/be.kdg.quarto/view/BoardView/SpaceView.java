package be.kdg.quarto.view.BoardView;

import be.kdg.quarto.view.PieceView.PieceView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SpaceView extends StackPane{
    private ImageView imageView;
    private Circle circle;
    public SpaceView () {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        imageView = new ImageView();
        circle = new Circle(18);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Circle getCircle() {
        return circle;
    }



    private void layoutNodes() {
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        getChildren().addAll(circle);
    }
}
