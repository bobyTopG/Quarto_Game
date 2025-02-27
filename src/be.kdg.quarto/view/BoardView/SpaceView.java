package be.kdg.quarto.view.BoardView;

import be.kdg.quarto.view.GameScreen.SelectView;
import be.kdg.quarto.view.PieceView.PieceView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SpaceView extends StackPane {
    private Circle circle;
    private boolean owned = false;

    public SpaceView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        circle = new Circle(18);
    }


    public Circle getCircle() {
        return circle;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned() {
        this.owned = true;
    }

    private void layoutNodes() {
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        getChildren().addAll(circle);
    }
    public void remove(SelectView view) {
        getChildren().remove(view);
    }
}
