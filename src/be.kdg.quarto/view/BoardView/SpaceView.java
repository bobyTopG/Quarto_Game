package be.kdg.quarto.view.BoardView;

import be.kdg.quarto.view.StartScreen.StartView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.swing.text.View;

public class SpaceView extends StackPane{
    private Circle circle;
    public SpaceView () {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        circle = new Circle(18);
    }

    private void layoutNodes() {
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        getChildren().add(circle);
    }
}
