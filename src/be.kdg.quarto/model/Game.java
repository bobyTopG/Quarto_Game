package src.be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {

    private Player ai;
    private Player human;
    private Player currentPlayer;
    private Board board;
    private Board pieceBoard;
    List<PieceType> pieceType = new ArrayList<>(Arrays.asList(PieceType.values()));


    public Game() {
        this(new Human("Bob", "secretPassword"),
                new Ai("Open Ai", AiLevel.HARD));
    }

    public Game(Player ai, Player human) {
        this.ai = ai;
        this.human = human;
        this.board = new Board();

    }

    public void newPiece() {
        Random random = new Random();
        if (!pieceType.isEmpty()) {
            board.setSelectedPieceType(pieceType.get(random.nextInt(pieceType.size())));
            pieceType.remove(board.getSelectedPieceType());
        }
    }

    private void switchTurns() {
        if (currentPlayer == ai) {
            currentPlayer = human;
        } else {
            currentPlayer = ai;
        }
    }

    public List<AiLevel> getAiLevels() {
        return Arrays.asList(AiLevel.values());
    }

    public void removeFromFillBoard() {
        board.remove(board.getSelectedPieceType());
    }

    public Player getAi() {
        return ai;
    }

    public Player getHuman() {
        return human;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }


    public void moveAiOrHuman(Player currentPlayer) {

    }

    // methods with business logic

    // necessary getters and setters
}
