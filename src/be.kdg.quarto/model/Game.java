package be.kdg.quarto.model;



import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player ai;
    private Player human;
    private Player currentPlayer;
    private Board board;
    private List<Piece> pieces;

    public Game() {
        this(new Human("Bob", "secretPassword"),
                new Ai("Open Ai", AiLevel.HARD, new PlayingStrategy("S1")));
    }

    public Game(Player ai, Player human) {
        this.ai = ai;
        this.human = human;
        this.board = new Board();
        pieces = new ArrayList<>();
        this.currentPlayer = human;
        cratePieces();
    }



    public void cratePieces() {
        for (Color color : Color.values()) {
            for (Fill fill : Fill.values()) {
                for (Height height : Height.values()) {
                    for (Shape shape : Shape.values()) {
                        Piece piece = new Piece(color, height, fill, shape);
                        pieces.add(piece);
                    }
                }
            }
        }
    }

    public List<Piece> getPieces() {
        return pieces;
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