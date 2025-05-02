package be.kdg.quarto;

import be.kdg.quarto.helpers.ErrorHelper;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        StartView view = new StartView();

        Scene scene = new Scene(view);
        loadStyleSheetsAndFonts(scene);

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


    /**
     * Loads all the styles & Fonts for all the pages, It will remain even after changing presenters
     * @param scene The scene where all the files are added
     */
    private void loadStyleSheetsAndFonts(Scene scene) {

        Font.loadFont(getClass().getResourceAsStream("/fonts/berlinBold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/berlin.ttf"), 12);

        //order is important, first loaded low priority, last loaded higher priority
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/choose-ai.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/continue-game.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/game.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/start-menu.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/statistics.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/settings.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/win.css")).toExternalForm());
        }catch (Exception e){
            ErrorHelper.showError(e,"Error loading one of the style sheets");
        }


    }

}
