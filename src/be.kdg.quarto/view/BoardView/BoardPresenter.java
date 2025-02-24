package be.kdg.quarto.view.BoardView;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardPresenter {

    private Game model;
    private BoardView view;

    public BoardPresenter(Game model, BoardView view) {
        this.model = model;
        this.view = view;
        updateView();
        addEventHandlers();


    }
    private void addEventHandlers() {
        for (SpaceView spaceView : view.getSpaceViews()) {
            spaceView.getCircle().setOnMouseClicked(event -> {
                System.out.println("Circle clicked!"); // Debug statement

                Random rand = new Random();
                if (!model.getPieces().isEmpty()) { // Check if pieces are available
                    int n = rand.nextInt(15);
                    String pic = model.getImages().get(model.getPieces().get(n));

                    ImageView imageView = new ImageView(new Image(pic));
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);

                    spaceView.getChildren().add(imageView);
                    // You might want to call updateView() here if needed
                } else {
                    System.out.println("No pieces available to display."); // Debug statement
                }
            });
        }
    }

    private void updateView() {


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                SpaceView spaceView = new SpaceView();
                view.getSpaceViews().add(spaceView);
                view.getSpaces().add(spaceView, i, j);
            }
        }


    }
}
