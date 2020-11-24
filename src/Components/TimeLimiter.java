package Components;

import java.util.Timer;

/**
 * This class represents a time limiter
 */
public class TimeLimiter {
    private long timeLimitInMs;//Time limit
    private boolean timeEnded;//Did the time limit reached its limit?
    private Timer timer;
    private long start;
    private long end;


    /**
     * The constructor
     *
     * @param timeLimitInMs - The time limit in ms
     */
    public TimeLimiter(long timeLimitInMs) {
        this.timeLimitInMs = timeLimitInMs;
        this.timeEnded = false;
    }

    /**
     * Did the time has reached its limit?
     *
     * @return
     */
    public boolean isTimeEnded() {
        return timeEnded;
    }

    /**
     * This function will start the TimeLimiter
     */
    public void start() {
        start(this.timeLimitInMs);
    }

    private void start(long timeLimitInMs) {
        this.timer = new Timer();
        start = System.currentTimeMillis();
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeEnded = true;
                        System.out.println(String.format("The task has reached its time limit (%d)", timeLimitInMs));
                    }
                },
                timeLimitInMs
        );
        // System.out.println("started with "+timeLimitInMs);
    }

    public void stop() {
        this.timer.cancel();

    }

    public void addMS(long addition) {
        end = System.currentTimeMillis();
        long timePassed = end - start;
        long timeRemain = this.timeLimitInMs - timePassed;

        long newTime = timeRemain + addition;

        if (newTime < 0) {
            timeEnded = true;
            return;
        }
        if (timeRemain < 0) {
            newTime = addition;
        }

        start(newTime);


    }
}
