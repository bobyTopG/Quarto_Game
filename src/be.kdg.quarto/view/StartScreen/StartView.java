package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.UiSettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class StartView extends BorderPane {

    private Label label;
    private Button newGame, continueButton, leaderBoard, statistics, quit;
    private BoardView board;
    private UiSettings settings;

    private static final int BUTTON_SPACING = 20;
    private static final int VBOX_LEFT_PADDING = 70;
    private static final int VBOX_TOP_PADDING = 10;
    private static final int VBOX_BOTTOM_PADDING = 20;
    private static final int LABEL_TOP_PADDING = 50;
    private static final int LABEL_LEFT_PADDING = 20;

    public StartView(UiSettings settings) {
        this.settings = settings;
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        board = new BoardView();
        label = createLabel("Quarto!");
        newGame = createButton("New Game", "new-game");
        continueButton = createButton("Continue", "continue-button");
        leaderBoard = createButton("Leaderboard", "leaderboard-button");
        statistics = createButton("Statistics", "statistics-button");
        quit = createButton("Quit", "quit-button");
        this.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(javafx.scene.text.Font.font("Sigmar", javafx.scene.text.FontWeight.BOLD, 40));
        return label;
    }

    private Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    private void layoutNodes() {
        VBox vbox = new VBox(BUTTON_SPACING);
        vbox.setPadding(new Insets(VBOX_TOP_PADDING, 10, VBOX_BOTTOM_PADDING, VBOX_LEFT_PADDING));
        vbox.getChildren().addAll(newGame, continueButton, leaderBoard, statistics, quit);
        vbox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        board.disableBoard();
        stackPane.getChildren().add(board);

        HBox hBox = new HBox(250, vbox, stackPane);
        label.setPadding(new Insets(10, 50, LABEL_TOP_PADDING, LABEL_LEFT_PADDING));

        setAlignment(label, Pos.CENTER);
        setTop(label);
        setCenter(hBox);
    }

    // Getters
    public BoardView getBoard() {
        return board;
    }

    public Button getNewGame() {
        return newGame;
    }

    public Button getStatistics() {
        return statistics;
    }

    public Button getQuit() {
        return quit;
    }
}