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
                new Ai("Open Ai", AiLevel.HARD , new PlayingStrategy("S1")));
    }

    public Game(Player ai, Player human) {
        this.ai = ai;
        this.human = human;
       // this.board = new Board();

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

    public Board getPieceBoard() {
        return pieceBoard;
    }

    public void setPieceBoard(Board pieceBoard) {
        this.pieceBoard = pieceBoard;
    }

    public List<PieceType> getPieceType() {
        return pieceType;
    }

    public void setPieceType(List<PieceType> pieceType) {
        this.pieceType = pieceType;
    }
}
