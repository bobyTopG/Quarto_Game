package be.kdg.quarto;

import javafx.stage.Stage;
import be.kdg.quarto.model.Game;

public class Main extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) {
        Game model = new Game();
       // StartView view = new StartView();
       //primaryStage.setScene(new Scene(view));
       // new StartPresenter(model, view);
        primaryStage.setHeight(400);
        primaryStage.setWidth(850);
        primaryStage.show();
    }

    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }
}
