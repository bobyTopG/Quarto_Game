package be.kdg.quarto.model;

import java.util.Date;

public class PausePeriod {
    private Date pauseStart;
    private Date pauseEnd;

    public PausePeriod(Date pauseStart, Date pauseEnd) {
        this.pauseStart = pauseStart;
        this.pauseEnd = pauseEnd;
    }

    public Date getPauseStart() {
        return pauseStart;
    }
    public Date getPauseEnd() {
        return pauseEnd;
    }

    public void setPauseEnd(Date pauseEnd) {
        this.pauseEnd = pauseEnd;
    }
    public void setPauseStart(Date pauseStart) {
        this.pauseStart = pauseStart;
    }

    public long getDurationInMillis() {
        if(pauseEnd != null)
            return pauseEnd.getTime() - pauseStart.getTime();
        else
            return (new Date().getTime() - pauseStart.getTime());
    }
}