package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;

import java.util.List;

public class Game {
    private Player ai;
    private Player human;
    private Player currentPlayer;
    private Board board;

    public Game() {
        this(new Human("Bob", "secretPassword"),
                new Ai("Open Ai", AiLevel.HARD, new PlayingStrategy("S1")));
    }

    public Game(Player ai, Player human) {
        this.ai = ai;
        this.human = human;
        this.board = new Board();
        this.currentPlayer = human;
    }

    public Player getAi() {
        return ai;
    }

    public void setAi(Player ai) {
        this.ai = ai;
    }

    public Player getHuman() {
        return human;
    }

    public void setHuman(Player human) {
        this.human = human;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Piece> getAvailablePieces() {
        return board.getAvailablePieces();
    }
}