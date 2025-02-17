package src.be.kdg.quarto.model;

import java.util.Date;

public class Move {
    Player player;
    int position;
    Piece piece;
    Piece selectedPiece;
    Date startTime;
    Date endTime;


    public Move(Player player, int position, Piece piece, Piece selectedPiece, Date startTime, Date endTime) {
        this.player = player;
        this.position = position;
        this.piece = piece;
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
}
//player: Player
//posX: int
//posY: int
//piece: Piece
//chosenPiece: Piece
//startTime: dateTime
//endTime: dateTime