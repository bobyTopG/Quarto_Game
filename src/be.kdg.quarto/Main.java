package src.be.kdg.quarto;

import javafx.application.Application;
import javafx.stage.Stage;
import src.be.kdg.quarto.model.Game;

public class Main extends Application {
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
        Application.launch(args);
    }
}
