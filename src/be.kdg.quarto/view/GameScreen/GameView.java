package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.helpers.CreateHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GameView extends BorderPane {
    private final GridPane boardGrid = new GridPane();
    private final StackPane rotatedBoardPane = new StackPane();

    private final GridPane selectGrid = new GridPane();

    VBox choosePieceBox;
    VBox buttonsBox;

    Button choosePieceConfirmation;
    Button backButton;

    private final BorderPane selectedPieceContainer = new BorderPane();
    private final ImageView selectedPieceImage = new ImageView();

    private final Label turn = CreateHelper.createLabel("Your Turn","turn-label");
    private final Label timer = new Label("00:00:00");
    private final Button quarto = CreateHelper.createButton("Quarto", new String[]{"game-button", "green-button","quarto-button"});
    private Button settingsButton;
    private final Button helpButton = CreateHelper.createButton("?", new String[]{"default-button","blue-button","help-button"});
    private final Button choosePiece = CreateHelper.createButton("Choose Piece", new String[]{"orange-button","game-button"});
    private final Button placePiece = CreateHelper.createButton("Place Piece", new String[]{ "blue-button", "game-button"});
    private final StackPane overlayContainer = new StackPane();
    private final SettingsView settingsView = new SettingsView();

    public GameView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {


        createBoard();

        createSelectedPieceHolder();

        // === Turn label ===

        createChoosePieceView();

        createSettingsButton();
        // === Overlay (settings screen) ===
        createSettingsScreen();
    }
    private void layoutNodes() {


        HBox placeBox = new HBox(placePiece, selectedPieceContainer);
        placeBox.setAlignment(Pos.CENTER);
        placeBox.setSpacing(10);


        Region vSpacer = new Region();
        // to make a separator from all the space left
        VBox.setVgrow(vSpacer, Priority.ALWAYS);
        buttonsBox = new VBox(vSpacer,placeBox,choosePiece);
        buttonsBox.setSpacing(15);
        buttonsBox.setPadding(new Insets(10, 20, 10, 30));
        buttonsBox.setAlignment(Pos.CENTER_LEFT);


        Region hSpacer = new Region();
        // to make a separator from all the space left
        HBox.setHgrow(hSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(turn, hSpacer, helpButton, settingsButton);

        topBar.setSpacing(10);
        topBar.setPadding(new Insets(10));

        this.setTop(topBar);

        this.setLeft(buttonsBox);

        //to move the board a bit up
        BorderPane.setMargin(rotatedBoardPane, new Insets(-100, -50, 0, 0));

        this.setCenter(rotatedBoardPane);


        VBox quartoBox = new VBox(quarto);
        quartoBox.setAlignment(Pos.BOTTOM_CENTER);
        //creating margin not to change the pos of other elements
        BorderPane.setMargin(quartoBox, new Insets(-15, 15, 15, -15));

        this.setRight(quartoBox);
        choosePiece.toFront();

        BorderPane.setMargin(overlayContainer, new Insets(-500, 0, 0, 0));
        this.setBottom(overlayContainer);
    }

    private void createSettingsScreen() {
        overlayContainer.setVisible(false);
        overlayContainer.setPrefHeight(500);
        overlayContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        StackPane.setAlignment(settingsView, Pos.CENTER);
        overlayContainer.getChildren().add(settingsView);

    }
    private void createSelectedPieceHolder() {
        VBox vbox = new VBox();
        vbox.setSpacing(5);

        Label label = CreateHelper.createLabel("Selected Piece", "piece-label");

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


        selectedPieceContainer.setCenter(pieceHolder);
        selectedPieceContainer.setBottom(label);
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
        this.setLeft(choosePieceBox);
    }

    void switchToMainSection(){
        this.setLeft(buttonsBox);
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

    BorderPane getSelectedPieceContainer() {
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

}