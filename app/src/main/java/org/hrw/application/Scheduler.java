package org.hrw.application;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    Timer timer;
    TimerTask timerTask;
    int timePeriod;

    public Scheduler(TimerTask timerTask, int timePeriod) {
        timer = new Timer();
        this.timerTask = timerTask;
        this.timePeriod = timePeriod;
    }

    public void executeTimer(Date firstExecution) {
        timer.schedule(timerTask, firstExecution, timePeriod * 1000L);
    }

    public void stop(){
        timer.cancel();
    }
}

