package org.hrw.application;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    Timer timer;
    TimerTask timerTask;
    int timePeriod;

    public Scheduler(SchedulerBuilder builder) {
        timer = new Timer();
        this.timerTask = builder.timerTask;
        this.timePeriod = builder.timePeriod;
    }

    public void executeTimer(Date firstExecution) {
        timer.schedule(timerTask, firstExecution, timePeriod * 1000L);
    }

    public void stop(){
        timer.cancel();
    }

    public static class SchedulerBuilder {
        TimerTask timerTask;
        int timePeriod;

        public SchedulerBuilder setTimerTask(TimerTask timerTask) {
            this.timerTask = timerTask;
            return this;
        }

        public SchedulerBuilder setTimePeriod(int timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        public Scheduler build() {
            return new Scheduler(this);
        }
    }
}

