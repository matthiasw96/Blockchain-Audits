package org.hrw.application;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    Timer timer;
    Processor processor;
    int timePeriod;

    public Scheduler(Processor processor, int timePeriod) {
        this.processor = processor;
        this.timePeriod = timePeriod;
    }

    public void executeTimer(Date firstExecution) {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                processor.run();
            }
        };
        timer.schedule(timerTask, firstExecution, timePeriod * 1000L);
    }

    public void stop(){
        timer.cancel();
        timer = null;
    }
}

