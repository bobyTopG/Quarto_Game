package src.be.kdg.quarto.model;

public class Tile {
    private int index;
    private Piece piece;


    public Tile(int index){
        this(index, null);
    }
    public Tile(int index , Piece piece) {
        this.index = index;
       setPiece(piece);
    }
    public int getIndex() {
        return index;
    }
    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
