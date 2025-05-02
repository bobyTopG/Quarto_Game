package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.view.SettingsScreen.SettingsView;
import be.kdg.quarto.view.StatisticsScreen.StatisticsView;
import be.kdg.quarto.view.WinScreen.WinView;
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
    private Label quartoText;
    private final GridPane selectGrid = new GridPane();

    VBox choosePieceBox;
    VBox leftBox;

    Button choosePieceConfirmation;
    Button backButton;


    private final HBox VSHBox = new HBox();
    private final VBox player1 = new VBox();
    private final Image playerImage;
    private final VBox player2 = new VBox();
    private final Image opponentImage;
    private final Label VSLabel = CreateHelper.createLabel("VS", "title");


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
    private final WinView winView = new WinView();

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
        turnStack.setMaxHeight(65);
        turnStack.getChildren().addAll(loadingBar, turn);
    }

    private void layoutNodes() {

        VBox buttonsBox = new VBox(placePiece, choosePiece);
        buttonsBox.setSpacing(25);

        HBox bottomBox = new HBox(buttonsBox, selectedPieceContainer);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(25);
        bottomBox.setMinHeight(150);


        Region vSpacer = new Region();
        // to make a separator from all the space left
        VBox.setVgrow(vSpacer, Priority.ALWAYS);


        timerContainer.getChildren().addAll(timer, timerLabel);
        timerContainer.setMaxHeight(200);
        timerContainer.setAlignment(Pos.CENTER);
        Region topSpacer = new Region();
        topSpacer.setPrefHeight(70);
        topSpacer.setMinHeight(70);

        leftBox = new VBox(topSpacer,VSHBox, timerContainer, bottomBox);
        BorderPane.setAlignment(leftBox, Pos.CENTER_LEFT);

        leftBox.setMaxWidth(400);
        leftBox.setSpacing(15);
        leftBox.setPadding(new Insets(10, 0, 10, 20));
        leftBox.setAlignment(Pos.CENTER);






        BorderPane.setMargin(rotatedBoardPane, new Insets(25, 0, 0, 20));

        root.setLeft(leftBox);

        root.setCenter(rotatedBoardPane);

        quartoText = CreateHelper.createLabel("Advise","choose-piece-label");




        Region hSpacer = new Region();
        // to make a separator from all the space left
        HBox.setHgrow(hSpacer, Priority.ALWAYS);
        VBox vBox = new VBox();
        HBox buttonBox = new HBox(helpButton, settingsButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.setSpacing(10);

        vBox.getChildren().addAll(buttonBox,quartoText);
        vBox.setAlignment(Pos.CENTER_RIGHT);
        HBox topBar = new HBox(turnStack,  hSpacer, vBox);

        topBar.setSpacing(10);
        topBar.setMaxHeight(100);
        topBar.setPadding(new Insets(15,10,-10,10));

        VBox quartoBox = new VBox(quarto);
        quartoBox.setAlignment(Pos.BOTTOM_CENTER);
        BorderPane.setMargin(quartoBox, new Insets(-15, 15, 15, -15));

        root.setRight(quartoBox);
        choosePiece.toFront();

        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(root,topBar,overlayContainer);
    }

    private void createSettingsScreen() {
        overlayContainer.setVisible(false);
        overlayContainer.setPrefHeight(500);
        overlayContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        StackPane.setAlignment(settingsView, Pos.CENTER);

    }
    void enableOverlay(){
        overlayContainer.setVisible(true);
        overlayContainer.getChildren().clear();

    }
    void showSettingsScreen() {
        enableOverlay();
        overlayContainer.getChildren().add(settingsView);
    }
    void showWinScreen(){
        enableOverlay();
        overlayContainer.getChildren().add(winView);

    }
    void showStatisticsScreen() {
        enableOverlay();
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
    }
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
        choosePieceBox.setPrefWidth(289);
        Region vSpacer = new Region();
        vSpacer.setMinHeight(60);
        vSpacer.setMaxHeight(60);
        choosePieceBox.getChildren().addAll(vSpacer,choosePieceLabel,selectGrid, buttonBox);

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

    WinView getWinView() { return  winView; }

    StatisticsView getStatisticsView() {
        return statisticsView;
    }


    ImageView getSelectedPieceImage() {
        return selectedPieceImage;
    }

    Label getTurn() {
        return turn;
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
    StackPane getTurnStack(){
        return turnStack;
    }

}