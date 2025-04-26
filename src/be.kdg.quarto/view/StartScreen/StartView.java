package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.FontHelper;
import be.kdg.quarto.helpers.ImageHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class StartView extends StackPane {

    private Label label;
    private Button register, login, leaderboard, quit, play;
    private HBox mainHBox;

    private VBox leftVBox;
    private BorderPane root;

    private Button online;
    private Label onlineText = CreateHelper.createLabel("","online-text");

    public StartView() {
        initialiseNodes();
        layoutNodes();
    }
    //private static final String FONT_FILE = "/fonts/berlin.ttf"; // Relative path

    private void initialiseNodes() {
        label = CreateHelper.createLabel("Quarto!", "main-title");
        label.setFont(FontHelper.getExtraLargeFont());
        root = new BorderPane();
        ///To DO fix names to register and log in!!!

        register = CreateHelper.createButton(AuthHelper.isLoggedIn() ? "New Game" : "Register",  new String[]{"green-button", "default-button"});
        login = CreateHelper.createButton(AuthHelper.isLoggedIn() ? "Continue" : "Login", new String[]{"orange-button", "default-button"});
        leaderboard = CreateHelper.createButton("Leaderboard", new String[]{"blue-button", "default-button"});
        quit = CreateHelper.createButton("Quit", new String[]{"red-button", "default-button"});
        mainHBox = CreateHelper.createHBox("root-hbox");
        play = CreateHelper.createButton("Play", new String[]{"green-button", "default-button","big-button"});
        leftVBox = CreateHelper.createVBox("start-main-vbox");

    }


    private void layoutNodes() {


        mainHBox.getChildren().add(leftVBox);
        //set the menu to Offline until not found connection
        switchOnlineMode(false);

        root.setCenter(mainHBox);
        VBox onlineVBox =  createOnlineButton();
        this.getChildren().addAll(root,onlineVBox);
        this.setAlignment(Pos.TOP_RIGHT);
    }

    public void loadBoardImage(Image boardImage) {
        StackPane board = new StackPane();
        ImageView boardImageView = new ImageView(boardImage); // No cast needed
        boardImageView.setFitHeight(400);
        boardImageView.setFitWidth(400);
        board.getChildren().add(boardImageView);
        mainHBox.getChildren().add(board);
    }
    void setOnlineButtonToConnecting(){
        onlineImageView.setImage(new Image(ImageHelper.getLoadingButtonPath()));
        online.setGraphic(onlineImageView);

    }

    void switchOnlineMode(boolean online) {
        if (online) {
            //because the VBoxes share the buttons we need to reassign them every time
            leftVBox.getChildren().clear();  // Clear first to prevent duplicates
            leftVBox.getChildren().addAll(label, register, login, leaderboard, quit);
        } else {
            leftVBox.getChildren().clear();  // Clear first to prevent duplicates
            leftVBox.getChildren().addAll(label, play, quit);
        }
    }
    ImageView onlineImageView;
    VBox createOnlineButton() {
        VBox onlineBox = new VBox();
        onlineBox.setAlignment(Pos.TOP_RIGHT);

        online = new Button();
        onlineImageView = new ImageView(new Image(ImageHelper.getLoadingButtonPath()));

        // Set the image size
        onlineImageView.setFitWidth(40);
        onlineImageView.setFitHeight(40);

        onlineImageView.setPreserveRatio(true);

        // Set the image on the button
        online.setGraphic(onlineImageView);
        onlineBox.setPadding(new Insets(10));
        onlineBox.setMaxWidth(150);      // Adjust to your desired size

        // Remove default button styling
        online.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");



        onlineBox.getChildren().addAll(online, onlineText);
        return onlineBox;
    }


    // Getters

     Button getRegister() {
        return register;
    }

     Button getLogin() {
        return login;
    }

     Button getLeaderboard() {
        return leaderboard;
    }

     Button getQuit() {
        return quit;
    }

    Button getOnline(){
        return online;
    }

    Button getPlay() {
        return play;
    }
    Label getOnlineText() {
        return onlineText;
    }
}