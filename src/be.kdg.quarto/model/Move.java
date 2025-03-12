package be.kdg.quarto.model;

import java.util.Date;

public class Move {

   private Player player;
   private int position;
   private Piece piece;
   private Piece selectedPiece;
   private int moveNumber;
   private Date startTime;
   private Date endTime;


    public Move(Player player, int position, Piece piece, int moveNumber, Date startTime, Date endTime) {
        this.player = player;
        this.position = position;
        this.piece = piece;
        this.moveNumber = moveNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Move(Player player, Piece selectedPiece,Date startTime, Date endTime){
        this.player = player;
        this.selectedPiece = selectedPiece;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }
    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public int getMoveNumber() { return moveNumber; }
    public void setMoveNumber(int moveNumber) { this.moveNumber = moveNumber; }

    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public float getDuration() {
        // todo: this is a placeholder return value
        return 0f;
    }


}
