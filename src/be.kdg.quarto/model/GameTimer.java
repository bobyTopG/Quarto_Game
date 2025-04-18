package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameTimer {
    private Date gameStartTime;
    private Date gameEndTime;
    private boolean isPaused = false;
    private Date currentPauseStart;
    private List<PausePeriod> pausePeriods = new ArrayList<>();
    private Move activeMove;  // Only track a single active move

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

    public GameTimer() {
        // Initialize with current time
        gameStartTime = new Date();
    }

    public void startGame() {
        gameStartTime = new Date();
    }

    public void endGame() {
        gameEndTime = new Date();
    }

    public void setActiveMove(Move move) {
        this.activeMove = move;
    }

    public void clearActiveMove() {
        this.activeMove = null;
    }

    public Move getActiveMove() {
        return this.activeMove;
    }

    public void pauseGame() {
        if (!isPaused) {
            isPaused = true;
            currentPauseStart = new Date();

            // Pause the active move if it exists
            if (activeMove != null) {
                activeMove.pause();
            }
        }
    }

    public void resumeGame() {
        if (isPaused) {
            Date pauseEnd = new Date();
            pausePeriods.add(new PausePeriod(currentPauseStart, pauseEnd));
            isPaused = false;
            currentPauseStart = null;

            // Resume the active move if it exists
            if (activeMove != null) {
                activeMove.resume();
            }
        }
    }

    public long getTotalPausedTimeInMillis() {
        long totalPausedTime = 0;

        // Add all completed pause periods
        for (PausePeriod pause : pausePeriods) {
            totalPausedTime += pause.getDurationInMillis();
        }

        // If we're currently paused, add the current pause time
        if (isPaused && currentPauseStart != null) {
            totalPausedTime += (new Date().getTime() - currentPauseStart.getTime());
        }

        return totalPausedTime;
    }
    public int getGameDurationInSeconds() {
        // Calculate end timestamp (current time if game isn't finished)
        long endTimestamp = (gameEndTime != null) ? gameEndTime.getTime() : new Date().getTime();

        // Calculate total duration excluding pauses (in milliseconds)
        long durationInMillis = endTimestamp - gameStartTime.getTime() - getTotalPausedTimeInMillis();

        return (int) (durationInMillis / 1000);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public Date getGameStartTime() {
        return gameStartTime;
    }

    public Date getGameEndTime() {
        return gameEndTime;
    }
}