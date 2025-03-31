package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.view.GameScreen.SettingsScreen.SettingsView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameView extends StackPane {
    private final GridPane boardGrid = new GridPane();
    private final StackPane rotatedBoardPane = new StackPane();
    private final StackPane[][] boardSpaces = new StackPane[4][4];

    private final GridPane selectGrid = new GridPane();
    private final ImageView[][] pieceSlots = new ImageView[4][4];

    private final StackPane selectedPieceContainer = new StackPane();
    private final ImageView selectedPieceImage = new ImageView();

    private final Label turn = new Label("Your Turn");
    private final Label timer = new Label("00:00:00");
    private final Button quarto = CreateHelper.createButton("Quarto", new String[]{"quarto-button", "default-button", "green-button"});
    private final Button settings = CreateHelper.createButton("Settings", new String[]{"settings-button", "default-button", "gray-button"});

    private final StackPane overlayContainer = new StackPane();
    private final SettingsView settingsView = new SettingsView();

    public GameView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        // === Board base elements (background + frame) ===
        Rectangle background = new Rectangle(250, 250);
        background.setArcWidth(50);
        background.setArcHeight(50);
        background.setFill(Color.web("#A77E33"));

        Circle frame = new Circle(110);
        frame.setFill(Color.TRANSPARENT);
        frame.setStroke(Color.WHITE);
        frame.setStrokeWidth(5);

        // === Board grid with spaces ===
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                StackPane cell = createBoardSpace();
                boardSpaces[row][col] = cell;
                boardGrid.add(cell, col, row);
            }
        }

        boardGrid.setHgap(5);
        boardGrid.setVgap(5);
        boardGrid.setAlignment(Pos.CENTER);
        // Stack it all in rotated pane
        rotatedBoardPane.getChildren().addAll(background, frame, boardGrid);
        rotatedBoardPane.setRotate(45);
        rotatedBoardPane.setPrefSize(300, 300);
        StackPane.setAlignment(boardGrid, Pos.CENTER);

        // === Select grid (right side) ===
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                ImageView pieceImage = new ImageView();
                pieceImage.setFitWidth(35);
                pieceImage.setFitHeight(35);
                pieceSlots[row][col] = pieceImage;

                StackPane pieceHolder = new StackPane(pieceImage);
                pieceHolder.setPrefSize(50, 50);
                selectGrid.add(pieceHolder, col, row);
                pieceHolder.setPadding(new Insets(10, 10, 10, 20));
                selectGrid.setGridLinesVisible(true);
            }
        }

        // === Selected piece display (top bar) ===
        Rectangle border = new Rectangle(50, 50, Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        selectedPieceImage.setFitHeight(35);
        selectedPieceImage.setFitWidth(35);
        selectedPieceContainer.getChildren().addAll(border, selectedPieceImage);

        // === Turn label ===
        turn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        turn.setTextFill(Color.WHITE);

        // === Overlay (settings screen) ===
        overlayContainer.setVisible(false);
        overlayContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        StackPane.setAlignment(settingsView, Pos.CENTER);
        overlayContainer.getChildren().add(settingsView);
    }

    private void layoutNodes() {
        VBox buttons = new VBox(25, quarto, settings);
        buttons.setPadding(new Insets(10, 20, 10, 10));
        buttons.setAlignment(Pos.CENTER);

        HBox mainContent = new HBox(100, buttons, rotatedBoardPane, selectGrid);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(10));

        GridPane topBar = new GridPane();
        topBar.setHgap(10);
        topBar.setVgap(10);
        topBar.setPadding(new Insets(10));
        topBar.add(createTurnPane(), 0, 0);
        topBar.add(selectedPieceContainer, 3, 0);
        topBar.add(timer, 30, 0);

        VBox layout = new VBox(25, topBar, mainContent);
        layout.setAlignment(Pos.CENTER);

        getChildren().addAll(layout, overlayContainer);
    }

    private StackPane createBoardSpace() {
        StackPane cell = new StackPane();
        Circle bg = new Circle(17.5);
        bg.setFill(Color.TRANSPARENT);
        bg.setStroke(Color.WHITE);
        cell.getChildren().addAll(bg);
        cell.setAlignment(Pos.CENTER);
        return cell;
    }

    private StackPane createTurnPane() {
        StackPane pane = new StackPane();
        Rectangle background = new Rectangle(120, 40);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(Color.web("#2dbdfa"));
        pane.getChildren().addAll(background, turn);
        return pane;
    }

    // Getters
    public StackPane getOverlayContainer() { return overlayContainer; }
    public SettingsView getSettingsView() { return settingsView; }

    public StackPane getSelectedPieceContainer() { return selectedPieceContainer; }
    public ImageView getSelectedPieceImage() { return selectedPieceImage; }

    public StackPane[][] getBoardSpaces() { return boardSpaces; }
    public ImageView[][] getSelectPieceImages() { return pieceSlots; }

    public Label getTurn() { return turn; }
    public Label getTimer() { return timer; }
    public Button getQuarto() { return quarto; }
    public Button getSettings() { return settings; }
}