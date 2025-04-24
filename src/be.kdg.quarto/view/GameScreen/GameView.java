package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.view.StatisticsScreen.StatisticsView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GameView extends StackPane {

    private final BorderPane root = new BorderPane();
    private final GridPane boardGrid = new GridPane();
    private final StackPane rotatedBoardPane = new StackPane();
    private Label quartoText = new Label();
    private final GridPane selectGrid = new GridPane();

    VBox choosePieceBox;
    VBox leftBox;

    Button choosePieceConfirmation;
    Button backButton;


    private final HBox VSHBox = new HBox();
    private VBox player1 = new VBox();
    private final Image playerImage;
    private final VBox player2 = new VBox();
    private final Image opponentImage;
    private Label VSLabel = CreateHelper.createLabel("VS", "title");


    private final VBox selectedPieceContainer = new VBox();
    private final ImageView selectedPieceImage = new ImageView();

    private final Label turn = CreateHelper.createLabel("Your Turn","turn-label");


    private final StackPane turnStack = new StackPane();
    private final ProgressBar loadingBar = new ProgressBar();


    private final Label timer = CreateHelper.createLabel("00:00","timer");
    private final Label timerLabel = CreateHelper.createLabel("Timer","timer-label");
    private final VBox  timerContainer = CreateHelper.createVBox("timer-container");
    private final Button quarto = CreateHelper.createButton("Quarto", new String[]{"game-button", "green-button","quarto-button"});


    private Button settingsButton;
    private final ToggleButton helpButton = CreateHelper.createToggleButton("?", new String[]{"default-button","help-button"});

    private final Button choosePiece = CreateHelper.createButton("Choose Piece", new String[]{"orange-button","game-button"});
    private final Button placePiece = CreateHelper.createButton("Place Piece", new String[]{ "blue-button", "game-button"});


    private final StackPane overlayContainer = new StackPane();
    private final SettingsView settingsView = new SettingsView();
    private final StatisticsView statisticsView = new StatisticsView();

    private final String opponentName;

    public GameView(Image player, Image opponent, String opponentName) {
        playerImage = player;
        opponentImage = opponent;
        this.opponentName = opponentName;
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {


        createBoard();

        createSelectedPieceHolder();

        // === Turn label ===

        createChoosePieceView();

        createSettingsButton();

        createLoadingButton();
        
        createVSHBox();
        // === Overlay (settings screen) ===
        createSettingsScreen();
    }

    private void createVSHBox() {
        ImageView player1Image = new ImageView(playerImage);
        player1Image.setFitHeight(70);
        player1Image.setFitWidth(70);
        Label playerLabel =  CreateHelper.createLabel("You","timer-label");

        player1.setAlignment(Pos.CENTER);
        player1.getChildren().addAll(player1Image,playerLabel);





        ImageView player2Image = new ImageView(opponentImage);
        player2Image.setFitHeight(70);
        player2Image.setFitWidth(70);
        Label opponentLabel =  CreateHelper.createLabel(opponentName,"timer-label");


        player2.getChildren().addAll(player2Image,opponentLabel);
        player2.setAlignment(Pos.CENTER);


        VBox VSBox = new VBox();
        VSBox.getChildren().addAll(VSLabel);
        VSBox.setAlignment(Pos.CENTER);

        VSHBox.setAlignment(Pos.CENTER);
        VSHBox.setSpacing(15);
        VSHBox.getChildren().addAll(player1,VSBox, player2);


        VSHBox.setPadding(new Insets(20,0,0,0));
        VSHBox.setMaxHeight(200);
        VSHBox.setMinHeight(0);
        VSHBox.setPrefHeight(200);
    }

    private void createLoadingButton() {

        loadingBar.getStyleClass().add("custom-progress-bar");

        turnStack.getChildren().addAll(loadingBar, turn);
    }

    private void layoutNodes() {

        VBox buttonsBox = new VBox(placePiece, choosePiece);
        buttonsBox.setSpacing(25);

        HBox bottomBox = new HBox(buttonsBox, selectedPieceContainer);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(25);
        bottomBox.setPadding(new Insets(0,0,20,0));
        bottomBox.setMinHeight(150);


        Region vSpacer = new Region();
        // to make a separator from all the space left
        VBox.setVgrow(vSpacer, Priority.ALWAYS);


        timerContainer.getChildren().addAll(timer, timerLabel);
        timerContainer.setAlignment(Pos.CENTER);

        leftBox = new VBox(VSHBox, timerContainer, bottomBox);
        leftBox.setSpacing(15);
        leftBox.setPadding(new Insets(10, 0, 20, 20));
        leftBox.setAlignment(Pos.CENTER);


        Region hSpacer = new Region();
        // to make a separator from all the space left
        HBox.setHgrow(hSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(turnStack,  hSpacer, helpButton, settingsButton);

        topBar.setSpacing(10);
        topBar.setPadding(new Insets(15,10,-10,10));

        root.setTop(topBar);

        root.setLeft(leftBox);

        //to move the board a bit up
        BorderPane.setMargin(rotatedBoardPane, new Insets(-100, -50, 0, 0));


        root.setCenter(rotatedBoardPane);
        quartoText.setManaged(false);
        quartoText = CreateHelper.createLabel("Advise","choose-piece-label");
        BorderPane.setMargin(quartoText, new Insets(0, 50, 0, -200));


        VBox quartoBox = new VBox( quartoText , quarto);
        quartoBox.setAlignment(Pos.BOTTOM_CENTER);
        quartoBox.setSpacing(270);
        //creating margin not to change the pos of other elements
        BorderPane.setMargin(quartoBox, new Insets(-15, 15, 15, -15));

        root.setRight(quartoBox);
        choosePiece.toFront();


        this.getChildren().addAll(root,overlayContainer);
    }

    private void createSettingsScreen() {
        overlayContainer.setVisible(false);
        overlayContainer.setPrefHeight(500);
        overlayContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        StackPane.setAlignment(settingsView, Pos.CENTER);

    }

    void showSettingsScreen() {
        overlayContainer.setVisible(true);
        overlayContainer.getChildren().clear();
        overlayContainer.getChildren().add(settingsView);
    }
    void showStatisticsScreen() {
        overlayContainer.setVisible(true);
        overlayContainer.getChildren().clear();
        overlayContainer.getChildren().add(statisticsView);
    }
    private void createSelectedPieceHolder() {

        Label label = CreateHelper.createLabel("Selected \nPiece :", "choose-piece-label");

        selectedPieceImage.setFitHeight(50);
        selectedPieceImage.setFitWidth(50);
        StackPane pieceHolder = new StackPane();

        // Add both elements to the StackPane (border first, then image)
        Rectangle border = new Rectangle(70, 70, Color.WHITE);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(3);
        pieceHolder.getChildren().addAll(border, selectedPieceImage);
        selectedPieceImage.setFitHeight(50);
        selectedPieceImage.setFitWidth(50);

        selectedPieceContainer.getChildren().addAll(pieceHolder,label);
        selectedPieceContainer.setSpacing(10);
    };
    void createChoosePieceView(){

         choosePieceConfirmation = CreateHelper.createButton("Choose Piece", new String[]{"default-button", "green-button"});
         backButton = CreateHelper.createButton("Back", new String[]{"default-button", "orange-button", "choose-piece-back-button"});
        Label choosePieceLabel = CreateHelper.createLabel("Choose a piece","choose-piece-label");



        HBox buttonBox = new HBox(backButton, choosePieceConfirmation);
        buttonBox.getStyleClass().add("choose-piece-button-box");


        selectGrid.getStyleClass().add("select-grid");


        choosePieceBox = new VBox();
        choosePieceBox.setSpacing(10);
        choosePieceBox.setAlignment(Pos.CENTER);

        choosePieceBox.getChildren().addAll(choosePieceLabel,selectGrid, buttonBox);

    }

    void switchToChoosePiece(){

        BorderPane.setMargin(choosePieceBox, new Insets(0, 0, 0, 10));
        root.setLeft(choosePieceBox);
    }

    void switchToMainSection(){
        root.setLeft(leftBox);
    }

    private void createSettingsButton(){
        Image image = new Image("images/buttons/Settings.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);

        Image hoverImage = new Image("images/buttons/Settings_Hover.png");
        ImageView hoverImageView = new ImageView(hoverImage);
        hoverImageView.setFitHeight(75);
        hoverImageView.setFitWidth(75);
        hoverImageView.setPreserveRatio(true);

        // Create a button with the ImageView
        settingsButton = new Button();
        settingsButton.setGraphic(imageView);
        settingsButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Hide any text

        settingsButton.setStyle("-fx-background-color: transparent;");
        settingsButton.setOnMouseEntered(e -> settingsButton.setGraphic(hoverImageView));
        settingsButton.setOnMouseExited(e -> settingsButton.setGraphic(imageView));

    }
    private void createBoard(){
        Rectangle background = new Rectangle(300, 300);
        background.setArcWidth(100);
        background.setArcHeight(100);
        background.setFill(Color.web("#A56F3F"));

        Circle frame = new Circle(135);
        frame.setFill(Color.TRANSPARENT);
        frame.setStroke(Color.WHITE);
        frame.setStrokeWidth(5);

        boardGrid.setHgap(2);
        boardGrid.setVgap(2);
        // === Board base elements (background + frame) ===

        boardGrid.setAlignment(Pos.CENTER);


        // Stack it all in rotated pane
        rotatedBoardPane.getChildren().addAll(background, frame, boardGrid);
        rotatedBoardPane.setRotate(45);
        rotatedBoardPane.setPrefSize(300, 300);

    }



    // Getters
    StackPane getOverlayContainer() {
        return overlayContainer;
    }

    SettingsView getSettingsView() {
        return settingsView;
    }

    StatisticsView getStatisticsView() {
        return statisticsView;
    }

    VBox getSelectedPieceContainer() {
        return selectedPieceContainer;
    }

    ImageView getSelectedPieceImage() {
        return selectedPieceImage;
    }

    Label getTurn() {
        return turn;
    }

    Label getTimer() {
        return timer;
    }
    void setTimer(int time) {
       int minutes = time / 60;
       int seconds = time % 60;
       timer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    Button getQuarto() {
        return quarto;
    }

    Button getSettings() {
        return settingsButton;
    }

    GridPane getSelectGrid() {
        return selectGrid;
    }

    Button getChoosePieceConfirmButton() {return  choosePieceConfirmation;}

    Button getBackButton() { return  backButton;}

    Button getChoosePiece() { return  choosePiece; }
    Button getPlacePiece(){
        return placePiece;
    }
    GridPane getBoardGrid() { return boardGrid; }

    Label getQuartoText() {
        return quartoText;
    }
    ToggleButton getHelpButton() {
        return helpButton;
    }

    ProgressBar getLoadingBar() {
        return loadingBar;
    }
    public Image getOpponentImage(){
        return opponentImage;
    }
    public Image getPlayerImage(){
        return playerImage;
    }

    public String getOpponentName(){
        return opponentName;
    }

}