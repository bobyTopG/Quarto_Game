package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Move {

    private Player player;
    private int position = -1;
    private Piece piece;
    private Piece selectedPiece;
    private int moveNumber = 1;
    private Date startTime;
    private Date endTime;
    private String warningMessage;

    private List<PausePeriod> pausePeriods = new ArrayList<>();
    private Date currentPauseStart;




    private static class PausePeriod {
        private Date pauseStart;
        private Date pauseEnd;

        public PausePeriod(Date pauseStart, Date pauseEnd) {
            this.pauseStart = pauseStart;
            this.pauseEnd = pauseEnd;
        }

        public long getDurationInMillis() {
            return pauseEnd.getTime() - pauseStart.getTime();
        }
    }
    public Move() {

    }
    public Move(Player player, int position, Piece piece, int moveNumber, Date startTime) {
        this.player = player;
        this.position = position;
        this.piece = piece;
        this.moveNumber = moveNumber;
        this.startTime = startTime;
    }

    public Move(Player player, Piece selectedPiece, Date startTime, Date endTime) {
        this.player = player;
        this.selectedPiece = selectedPiece;
        this.startTime = startTime;
        this.endTime = endTime;
    }



    // Pause functionality
    public void pause() {
        if (currentPauseStart == null) {
            // Only set pause start if we're not already paused
            currentPauseStart = new Date();
        }
    }

    public void resume() {
        if (currentPauseStart != null) {
            // Add the completed pause period to our list
            pausePeriods.add(new PausePeriod(currentPauseStart, new Date()));
            currentPauseStart = null;
        }
    }

    // Calculate total paused time in milliseconds
    private long getTotalPausedTimeInMillis() {
        long totalPausedTime = 0;

        // Add all completed pause periods
        for (PausePeriod pause : pausePeriods) {
            totalPausedTime += pause.getDurationInMillis();
        }

        // If we're currently paused, add the current pause time
        if (currentPauseStart != null) {
            totalPausedTime += (new Date().getTime() - currentPauseStart.getTime());
        }

        return totalPausedTime;
    }



    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
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

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
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

    public float getDuration() {
        if (startTime == null) return 0f;

        // Calculate end timestamp (current time if move isn't finished)
        long endTimestamp = (endTime != null) ? endTime.getTime() : new Date().getTime();

        // Calculate total duration excluding pauses (in milliseconds)
        long durationInMillis = endTimestamp - startTime.getTime() - getTotalPausedTimeInMillis();

        // Convert to seconds as float
        return durationInMillis / 1000.0f;
    }

    public boolean isPositionSet() {
        return position >= 0;
    }

    public boolean isPieceSet() {
        return piece != null || selectedPiece != null;
    }

    @Override
    public String toString() {
        return "Move{" +
                "player=" + player +
                ", position=" + position +
                ", piece=" + piece +
                ", selectedPiece=" + selectedPiece +
                ", moveNumber=" + moveNumber +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }


}
