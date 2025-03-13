package be.kdg.quarto.view.BoardView;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BoardSpaceView extends StackPane {
    private Circle circle;

    public BoardSpaceView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        circle = new Circle(18);
    }


    public Circle getCircle() {
        return circle;
    }


    private void layoutNodes() {
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        getChildren().addAll(circle);
    }

    public void reset() {
        getChildren().clear();
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        getChildren().addAll(circle);

    }
}
