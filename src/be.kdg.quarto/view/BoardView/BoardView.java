package be.kdg.quarto.view.BoardView;

import be.kdg.quarto.view.StartScreen.StartView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;


public class BoardView extends StackPane {
    private Rectangle background;
    private Circle frame;
    private GridPane spaces;


    public BoardView() {
        initialiseNodes();
        layoutNodes();
    }

    public GridPane getSpaces() {
        return spaces;
    }

    private void initialiseNodes() {
        spaces = new GridPane();
        background = new Rectangle(250, 250);
        background.setArcWidth(50);
        background.setArcHeight(50);
        frame = new Circle(110);
        frame.setFill(Color.TRANSPARENT);
        frame.setStroke(Color.WHITE);
        frame.setStrokeWidth(5);
    }

    private void layoutNodes() {
        Rotate rotate = new Rotate(45, background.getX() + background.getWidth() / 2, background.getY() + background.getHeight() / 2);
        background.getTransforms().add(rotate);
        spaces.getTransforms().add(rotate);
        spaces.setPadding(new Insets(70, 20, 120, 100));
        background.setFill(Color.web("#A77E33"));
        spaces.setAlignment(Pos.CENTER);
        getChildren().addAll(background, spaces, frame);

    }
}