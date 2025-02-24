package be.kdg.quarto;

import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import be.kdg.quarto.model.Game;

public class Main extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) {
        Game model = new Game();
        StartView view = new StartView();
        primaryStage.setScene(new Scene(view));
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        new StartPresenter(model, view);
        primaryStage.setHeight(400);
        primaryStage.setWidth(550);
        primaryStage.show();
    }

    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }
}
