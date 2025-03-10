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

    public Board getBoard() {
        return board;
    }

    private final Board board;
    private Piece selectedPiece;

    public Game() {
        this(new Human("Bob", "secretPassword"),
                new Ai("Open Ai", AiLevel.HARD, new RandomPlayingStrategy(null)));
    }

    public Game(Player human, Player ai) {
        this.human = human;
        this.ai = ai;
        this.board = new Board();
        this.currentPlayer = ai;
    }



    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurns() {
        currentPlayer = (currentPlayer == human) ? ai : human;
    }


    public Player getHuman() {
        return human;
    }


    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }





    public List<Piece> getPieces() {
        return board.getAvailablePieces();
    }
}