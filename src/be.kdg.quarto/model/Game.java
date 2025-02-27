package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;

import java.util.List;
import java.util.Random;

public class Game {
    private final Player ai;
    private final Player human;
    private Player currentPlayer;
    private final Board board;
    private Piece selectedPiece;

    public Game() {
        this(new Human("Bob", "secretPassword"),
                new Ai("Open Ai", AiLevel.HARD, new PlayingStrategy("S1")));
    }

    public Game(Player human, Player ai) {
        this.human = human;
        this.ai = ai;
        this.board = new Board();
        this.currentPlayer = ai;  // This could be parameterized
        initializePieces();
    }

    // Properly named method
    private void initializePieces() {
        for (Color color : Color.values()) {
            for (Fill fill : Fill.values()) {
                for (Height height : Height.values()) {
                    for (Shape shape : Shape.values()) {
                        board.getAvailablePieces().add(new Piece(color, height, fill, shape));
                    }
                }
            }
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurns() {
        currentPlayer = (currentPlayer == human) ? ai : human;
    }

    public boolean isAiTurn() {
        return currentPlayer.equals(ai);
    }

    public boolean isHumanTurn() {
        return currentPlayer.equals(human);
    }

    public Player getAi() {
        return ai;
    }

    public Player getHuman() {
        return human;
    }

    public Board getBoard() {
        return board;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public void selectRandomPiece() {
        List<Piece> availablePieces = board.getAvailablePieces();
        if (!availablePieces.isEmpty()) {
            Random rand = new Random();
            selectedPiece = availablePieces.get(rand.nextInt(availablePieces.size()));
            board.removePiece(selectedPiece);
        }
    }

    public List<Piece> getPieces() {
        return board.getAvailablePieces();
    }
}