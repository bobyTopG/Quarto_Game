package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.view.BoardView.BoardView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameView extends StackPane {

    private PieceView piece;
    private SelectPieceView selectView;
    private Label turn, timer;
    private Button quarto,settings;
    private BoardView board;

    // Constants for padding, spacing, and styling
    private static final int BUTTON_SPACING = 25;
    private static final int VBOX_LEFT_PADDING = 10;
    private static final int VBOX_TOP_PADDING = 10;
    private static final int VBOX_BOTTOM_PADDING = 10;
    private static final int HBOX_SPACING = 100;
    private static final int GRID_HGAP = 10;
    private static final int GRID_VGAP = 10;

    public GameView() {
        initialiseNodes();
        layoutNodes();
    }

    // Getters
    public SelectPieceView getSelectView() {
        return selectView;
    }

    public BoardView getBoard() {
        return board;
    }

    public PieceView getPiece() {
        return piece;
    }

    public Label getTurn() {
        return turn;
    }

    private void initialiseNodes() {
        selectView = new SelectPieceView();
        board = new BoardView();
        piece = new PieceView();
        turn = createLabel("Your Turn", 20, FontWeight.BOLD, Color.WHITE);
        timer = new Label("00:00:00");
        quarto = createButton("Quarto", "quarto-button");
        settings = createButton("Settings", "settings-button");

        // Apply CSS stylesheet
        this.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
    }

    // Helper method to create a button with text and style
    private Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    // Helper method to create a label with specific font and text color
    private Label createLabel(String text, int fontSize, FontWeight fontWeight, Color textColor) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", fontWeight, fontSize));
        label.setTextFill(textColor);
        return label;
    }

    private void layoutNodes() {
        VBox vbox = new VBox(BUTTON_SPACING);
        vbox.setPadding(new Insets(VBOX_TOP_PADDING, VBOX_LEFT_PADDING, VBOX_BOTTOM_PADDING, VBOX_LEFT_PADDING));
        vbox.getChildren().addAll(quarto, settings);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10,200,10,10));

        HBox hbox = new HBox(HBOX_SPACING);
        board.enableBoard();

        hbox.getChildren().addAll(vbox, board, selectView);

        GridPane grid = new GridPane();
        grid.setHgap(GRID_HGAP);
        grid.setVgap(GRID_VGAP);
        grid.setPadding(new Insets(10));

        StackPane pane = createTurnPane();
        grid.add(pane, 0, 0);
        grid.add(piece, 3, 0);
        grid.add(timer, 30, 0);

        VBox vbox2 = new VBox();
        vbox2.setSpacing(BUTTON_SPACING);
        vbox2.getChildren().addAll(grid, hbox);
        getChildren().add(vbox2);
    }

    // Helper method to create the turn pane with a rounded rectangle background
    private StackPane createTurnPane() {
        StackPane pane = new StackPane();
        Rectangle r = new Rectangle(120, 40);
        r.setArcWidth(20);
        r.setArcHeight(20);
        r.setFill(Color.web("#2dbdfa"));
        pane.getChildren().addAll(r, turn);
        return pane;
    }
}