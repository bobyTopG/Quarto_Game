package be.kdg.quarto.view.BoardView;

import be.kdg.quarto.view.StartScreen.StartView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;


public class BoardView extends Pane {
    private Rectangle background;
    private Circle frame;
    private GridPane spaces;
    private List<SpaceView> spaceViews =new ArrayList<>();
    StackPane stackPane = new StackPane();

    public List<SpaceView> getSpaceViews() {
        return spaceViews;
    }


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
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                SpaceView spaceView = new SpaceView();
                getSpaceViews().add(spaceView);
                getSpaces().add(spaceView, i, j);
            }
        }
    }

    private void layoutNodes() {

        Rotate rotate = new Rotate(45, 0, 0);
        spaces.setPadding(new Insets(50, 50, 50, 50));
        background.setFill(Color.web("#A77E33"));
        stackPane.getTransforms().add(rotate);
        stackPane.setPrefSize(background.getWidth(), background.getHeight());
        getChildren().add(stackPane);
    }

    public void disableBoard(){
        stackPane.getChildren().addAll(background,spaces,frame);
    }
    public  void  enableBoard(){
        stackPane.getChildren().addAll(background,frame,spaces);
    }
}