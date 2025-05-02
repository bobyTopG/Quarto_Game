package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Move {

    private Player player;
    private int position = -1;
    private Piece piece;
    private Piece selectedPiece;
    private int moveNumber;
    private Date startTime;
    private Date endTime;
    private String warningMessage;

    private final List<PausePeriod> pausePeriods = new ArrayList<>();
    private Date currentPauseStart;

    public Move() {

    }

    public Move(Player player, Piece piece, Piece selectedPiece, int position, int moveNumber, Date startTime, Date endTime) {
        this.player = player;
        this.piece = piece;
        this.selectedPiece = selectedPiece;
        this.position = position;
        this.moveNumber = moveNumber;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    /**
     * pauses the move, used only for the current move
     */
    public void pause() {
        if (currentPauseStart == null) {
            // Only set pause start if we're not already paused
            currentPauseStart = new Date();
        }
    }

    /**
     *  resumes the moves, adds pausePeriod to move, used only for the current move
     */
    public void resume() {
        if (currentPauseStart != null) {
            // Add the completed pause period to our list
            pausePeriods.add(new PausePeriod(currentPauseStart, new Date()));
            currentPauseStart = null;
        }
    }

    /**
     * Calculates total paused time in milliseconds
     */
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


    /**
     * message that gives hints for this move
     */
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

    /**
     * gets the duration of the move
     * @return
     */
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

    public List<PausePeriod> getPausePeriods() {
        return pausePeriods;
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
