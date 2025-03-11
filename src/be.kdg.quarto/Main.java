package be.kdg.quarto;

import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import be.kdg.quarto.view.UiSettings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import be.kdg.quarto.model.Game;

import java.net.MalformedURLException;
import java.nio.file.Files;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        UiSettings uiSettings = new UiSettings();
        Game model = new Game();
        StartView view = new StartView(uiSettings);

        Scene scene = new Scene(view);
        loadStyleSheets(scene);

        if (uiSettings.styleSheetAvailable()){
            try {
                scene.getStylesheets().add(uiSettings.getStyleSheetPath().toUri().toURL().toString());
            } catch (MalformedURLException ex) {
                // do nothing, if toURL-conversion fails, program can continue
            }
        }
        primaryStage.setScene(scene);
        //primaryStage.setHeight(uiSettings.getLowestRes() / 4);
        //primaryStage.setWidth(uiSettings.getLowestRes() / 4);
        primaryStage.setTitle(uiSettings.getApplicationName());
        primaryStage.setResizable(false);



        primaryStage.setScene(scene);
        new StartPresenter(model, view);
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

    }
}
