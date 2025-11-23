package org.hrw.application;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Schedules and periodically triggers execution of the {@link Processor}.
 *
 * <p>This class uses a {@link Timer} to run the processing workflow at a fixed
 * interval. It is typically started once during application startup and controls
 * the automation of data collection, hashing and blockchain anchoring.</p>
 */
public class Scheduler {
    Timer timer;
    Processor processor;
    int timePeriod;

    public Scheduler(Processor processor, int timePeriod) {
        this.processor = processor;
        this.timePeriod = timePeriod;
    }

    /**
     * <p>Starts the periodic execution of the processor.
     * Creates a new instance of the timer task with each execution.</p>
     *
     * @param firstExecution the date and time of the first execution
     */
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

