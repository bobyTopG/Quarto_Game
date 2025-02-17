package src.be.kdg.quarto.model;

public class Piece {
   PieceType type;

    private int position = 0;

    public Piece(int position , PieceType type) {
        this.position = position;
        this.type = type;
    }


    public int getPosition() {
        return position;
    }

    public PieceType getType() {
        return type;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
