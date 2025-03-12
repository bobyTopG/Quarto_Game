package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;
import javafx.scene.control.Alert;

public class Game {
    private final Ai ai;
    private final Human human;
    private final GameSession gameSession;
    private Player currentPlayer;
    private final Board tilesToSelect;
    private final Board tilesToPlace;
    private Tile currentTile = new Tile();

    public Game() {
        this(new Human("Bob", "secretPassword"), new Ai("Open Ai", AiLevel.HARD, null, "Description"));
    }

    public Game(Human human, Ai ai) {
        this.human = human;
        this.ai = ai;
        this.tilesToSelect = new Board();
        this.tilesToPlace = new Board();
        tilesToSelect.generateAllTiles();
        tilesToPlace.createEmptyTiles();

        this.gameSession = new GameSession(human, ai);
        this.currentPlayer = ai;
        ai.setStrategy(new RandomPlayingStrategy(tilesToSelect, tilesToPlace));

        if (isAiTurn()) {
            handleAiTurn();
        }
    }

    public Piece createPieceFromImageName(String path) {
        String filename = path.substring(path.indexOf("/pieces/") + 8, path.lastIndexOf("."));
        String[] parts = filename.split("_");

        if (parts.length != 4) throw new IllegalArgumentException("Invalid image name format: " + path);

        try {
            return new Piece(
                    Color.valueOf(parts[2].toUpperCase()),
                    Height.valueOf(parts[3].toUpperCase()),
                    Fill.valueOf(parts[0].toUpperCase()),
                    Shape.valueOf(parts[1].toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid enum value in image name.");
            return null;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurns() {
        if (gameSession.hasWinner()) return;
        currentPlayer = gameSession.getOtherPlayer(currentPlayer);
        if (isAiTurn()) {
            handleAiTurn();
        }
    }

    private void handleAiTurn() {
        if (currentPlayer == ai) {
            if (currentTile.getPiece() != null) {
                //Placing
                ai.getStrategy().placePiece().setPiece(currentTile.getPiece());
                getCurrentTile().setPiece(null);
                handleAiTurn();
            } else {
                //Picking
                try {


                    getCurrentTile().setPiece(ai.getStrategy().selectPiece().getPiece());
                    getTilesToSelect().getTiles()
                            .get(getTilesToSelect().getTiles()
                                    .indexOf(getCurrentTile())).setPiece(null);
                    switchTurns();
                } catch (NullPointerException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText("There are no pieces to select!");
                    alert.showAndWait();
                }
            }
        }
    }

    private boolean isAiTurn() {
        return currentPlayer == ai;
    }

    public Board getPlacedTiles() {
        return tilesToPlace;
    }

    public Board getTilesToSelect() {
        return tilesToSelect;
    }

    public Player getHuman() {
        return human;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile selectedPiece) {
        this.currentTile = selectedPiece;
    }
}