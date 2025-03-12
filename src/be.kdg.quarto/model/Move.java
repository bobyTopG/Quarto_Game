package be.kdg.quarto.model;

public class Move {
    private Tile tile;  // The tile being placed
    private int position;  // The position on the board (index)
    private int evaluation;  // The evaluation score for minimax
    private boolean isMaximizingPlayer;  // Whether the current move is for the maximizing player


    public Move(Tile tile, int position, int evaluation, boolean isMaximizingPlayer) {
        this.tile = tile;
        this.position = position;
        this.evaluation = evaluation;
        this.isMaximizingPlayer = isMaximizingPlayer;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }


    public boolean isMaximizingPlayer() {
        return isMaximizingPlayer;
    }

    public void setMaximizingPlayer(boolean isMaximizingPlayer) {
        this.isMaximizingPlayer = isMaximizingPlayer;
    }


    public Move copy() {
        return new Move(this.tile, this.position, this.evaluation, this.isMaximizingPlayer);
    }

    @Override
    public String toString() {
        return "Move [tile=" + tile + ", position=" + position + ", evaluation=" + evaluation + "]";
    }
}