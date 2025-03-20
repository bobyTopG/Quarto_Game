package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.FontHelper;
import be.kdg.quarto.view.BoardView.BoardView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class StartView extends BorderPane {

    private Label label;
    private Button registerB ,loginB, continueButton, leaderBoard, statistics, quit;
    private HBox mainHBox;
    private BoardView board;


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
        board = new BoardView();
        label = CreateHelper.createLabel("Quarto!", "main-title");
        label.setFont(FontHelper.getExtraLargeFont());

        registerB = CreateHelper.createButton("New Game",  new String[]{"green-button", "default-button"});
        loginB = CreateHelper.createButton("Continue", new String[]{"orange-button", "default-button"});
        continueButton = CreateHelper.createButton("Continue", new String[]{"orange-button", "default-button"});
        leaderBoard = CreateHelper.createButton("Leaderboard", new String[]{"blue-button", "default-button"});
        statistics = CreateHelper.createButton("Statistics", new String[]{"blue-button", "default-button"});
        quit = CreateHelper.createButton("Quit", new String[]{"red-button", "default-button"});
        mainHBox = CreateHelper.createHBox("root-hbox");
    }


    private void layoutNodes() {
        VBox vbox = CreateHelper.createVBox("start-main-vbox");
        vbox.setPadding(new Insets(VBOX_TOP_PADDING, 10, VBOX_BOTTOM_PADDING, VBOX_LEFT_PADDING));
        vbox.getChildren().addAll(label, registerB ,loginB, continueButton, leaderBoard, statistics, quit);
        vbox.setAlignment(Pos.CENTER);


        mainHBox.getChildren().add(vbox);

        setCenter(mainHBox);
    }

    // Getters
    public BoardView getBoard() {
        return board;
    }

    public void loadBoardImage(Image boardImage) {
        StackPane stackPane = new StackPane();
        ImageView boardImageView = new ImageView(boardImage); // No cast needed
        boardImageView.setFitHeight(400);
        boardImageView.setFitWidth(400);
        stackPane.getChildren().add(boardImageView);
        mainHBox.getChildren().add(stackPane);
    }

    public Button getLoginB() {
        return loginB;
    }

    public Button getRegisterB() {
        return registerB;
    }

    public Button getStatistics() {
        return statistics;
    }

    public Button getQuit() {
        return quit;
    }
}