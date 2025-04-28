package be.kdg.quarto;

import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.MalformedURLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        StartView view = new StartView();

        Scene scene = new Scene(view);
        loadStyleSheets(scene);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        new StartPresenter(view,true);
        primaryStage.setHeight(500);
        primaryStage.setWidth(900);
        primaryStage.show();
    }

    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }

    private void loadStyleSheets(Scene scene) {
        //order is important, first loaded low priority, last loaded higher priority
        scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/style/choose-ai-view.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/style/game.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/style/start-menu.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/style/statistics.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/style/settings.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/style/win.css").toExternalForm());


    }
}
