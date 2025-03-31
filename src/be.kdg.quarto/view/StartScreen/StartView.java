package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.FontHelper;
import be.kdg.quarto.view.UiSettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class StartView extends BorderPane {

    private Label label;
    private Button newGame, continueButton, leaderboard, statistics, quit;
    private HBox mainHBox;
    private UiSettings settings;

    private static final int BUTTON_SPACING = 20;
    private static final int VBOX_LEFT_PADDING = 70;
    private static final int VBOX_TOP_PADDING = 10;
    private static final int VBOX_BOTTOM_PADDING = 20;
    public StartView() {
        initialiseNodes();
        layoutNodes();
    }
    //private static final String FONT_FILE = "/fonts/berlin.ttf"; // Relative path

    private void initialiseNodes() {
        label = CreateHelper.createLabel("Quarto!", "main-title");
        label.setFont(FontHelper.getExtraLargeFont());

        newGame = CreateHelper.createButton(AuthHelper.isLoggedIn() ? "New Game" : "Register",  new String[]{"green-button", "default-button"});
        continueButton = CreateHelper.createButton(AuthHelper.isLoggedIn() ? "Continue" : "Login", new String[]{"orange-button", "default-button"});
        leaderboard = CreateHelper.createButton("Leaderboard", new String[]{"blue-button", "default-button"});
        statistics = CreateHelper.createButton("Statistics", new String[]{"blue-button", "default-button"});
        quit = CreateHelper.createButton("Quit", new String[]{"red-button", "default-button"});
        mainHBox = CreateHelper.createHBox("root-hbox");
    }


    private void layoutNodes() {
        VBox vbox = CreateHelper.createVBox("start-main-vbox");
        vbox.setPadding(new Insets(VBOX_TOP_PADDING, 10, VBOX_BOTTOM_PADDING, VBOX_LEFT_PADDING));
        vbox.getChildren().addAll(label, newGame, continueButton, leaderboard, statistics, quit);
        vbox.setAlignment(Pos.CENTER);


        mainHBox.getChildren().add(vbox);

        setCenter(mainHBox);
    }


    public void loadBoardImage(Image boardImage) {
        StackPane stackPane = new StackPane();
        ImageView boardImageView = new ImageView(boardImage); // No cast needed
        boardImageView.setFitHeight(400);
        boardImageView.setFitWidth(400);
        stackPane.getChildren().add(boardImageView);
        mainHBox.getChildren().add(stackPane);
    }
    // Getters

    public Button getNewGame() {
        return newGame;
    }

    public Button getContinueButton() {
        return continueButton;
    }

    public Button getStatistics() {
        return statistics;
    }

    public Button getLeaderboard() {
        return leaderboard;
    }

    public Button getQuit() {
        return quit;
    }
}