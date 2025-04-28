package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameTimer {
    private final Game game;
    private Date gameStartTime;
    private Date gameEndTime;
    private boolean isPaused = false;
    private Date currentPauseStart;
    private final List<PausePeriod> pausePeriods = new ArrayList<>();

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
            Date pauseEnd = new Date();
            pausePeriods.add(new PausePeriod(currentPauseStart, pauseEnd));
            isPaused = false;
            currentPauseStart = null;
            game.getCurrentMove().resume();

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