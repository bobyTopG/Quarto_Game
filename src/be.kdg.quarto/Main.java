package be.kdg.quarto;

import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import be.kdg.quarto.view.UiSettings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

        Scene scene = new Scene(view, 800, 600);

        if (uiSettings.styleSheetAvailable()){
            try {
                scene.getStylesheets().add(uiSettings.getStyleSheetPath().toUri().toURL().toString());
            } catch (MalformedURLException ex) {
                // do nothing, if toURL-conversion fails, program can continue
            }
        }
        primaryStage.setScene(scene);
        primaryStage.setHeight(uiSettings.getLowestRes() / 4);
        primaryStage.setWidth(uiSettings.getLowestRes() / 4);
        primaryStage.setTitle(uiSettings.getApplicationName());
        if (Files.exists(uiSettings.getApplicationIconPath())) {
            try {
                primaryStage.getIcons().add(new Image(uiSettings.getApplicationIconPath().toUri().toURL().toString()));
            }
            catch (MalformedURLException ex) {
                // do nothing, if toURL-conversion fails, program can continue
            }
        } else { // do nothing, if ApplicationIcon is not available, program can continue
        }

        primaryStage.setScene(scene);
        new StartPresenter(model, view);
        primaryStage.setHeight(500);
        primaryStage.setWidth(1000);
        primaryStage.show();
    }

    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }
}
