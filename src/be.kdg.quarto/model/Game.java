package be.kdg.quarto.model;


import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game {
    private Player ai;
    private Player human;



    private Player currentPlayer;
    private Board board;
    private Piece randomPiece;


    public Game() {
        this(new Human("Bob", "secretPassword"),
                new Ai("Open Ai", AiLevel.HARD, new PlayingStrategy("S1")));
    }

    public Game(Player human, Player ai) {
        this.ai = ai;
        this.human = human;
        this.board = new Board();
        this.currentPlayer = human;
        cratePieces();

    }

    public void cratePieces() {
        for (Color color : Color.values()) {
            for (Fill fill : Fill.values()) {
                for (Height height : Height.values()) {
                    for (Shape shape : Shape.values()) {
                        Piece piece = new Piece(color, height, fill, shape);
                        board.getAvailablePieces().add(piece);
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

    public Player getAi() {
        return ai;
    }

    public Board getBoard() {
        return board;
    }

    public Player getHuman() {
        return human;
    }

    public Piece getRandomPiece() {
        return randomPiece;
    }

    public void selectRandomPiece() {
        Random rand = new Random();
        randomPiece = board.getAvailablePieces().get(rand.nextInt(board.getAvailablePieces().size()));
      board.removePiece(randomPiece);
    }

    public List<Piece> getPieces() {
        return board.getAvailablePieces();
    }



}