package be.kdg.quarto.model;

import java.util.Date;

public class GameTimer {
    private final Game game;
    //we do not need end Time as this timer is used only while Game session is not completed
    private final Date gameStartTime;
    private boolean isPaused = false;
    private Date currentPauseStart;

    public GameTimer(Game game) {
        // Initialize with current time
        gameStartTime = new Date();
        this.game = game;
    }

    public GameTimer(Game game, Date startTime) {
        // Initialize with current time
        this.gameStartTime = startTime;
        this.game = game;
    }


    public void pauseGame() {
        if (!isPaused) {
            isPaused = true;
            currentPauseStart = new Date();
            game.getCurrentMove().pause();

        }
    }

    public void resumeGame() {
        if (isPaused) {
            isPaused = false;
            currentPauseStart = null;
            game.getCurrentMove().resume();

        }
    }

    public long getTotalPausedTimeInMillis() {
        long totalPausedTime = 0;

        // Add all completed pause periods
        for(Move move : game.getMoves()){
            for(PausePeriod pausePeriod : move.getPausePeriods()){
                totalPausedTime += pausePeriod.getDurationInMillis();
            }
        }

        // If we're currently paused, add the current pause time
        if (isPaused && currentPauseStart != null) {
            totalPausedTime += (new Date().getTime() - currentPauseStart.getTime());
        }

        return totalPausedTime;
    }

    public int getGameDurationInSeconds() {

        // Calculate total duration excluding pauses (in milliseconds)
        long durationInMillis = new Date().getTime() - gameStartTime.getTime() - getTotalPausedTimeInMillis();

        return (int) (durationInMillis / 1000);
    }


    public Date getCurrentPauseStart() {
        return currentPauseStart;
    }
}