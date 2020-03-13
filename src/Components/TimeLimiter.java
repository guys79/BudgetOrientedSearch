package Components;
import java.util.Timer;
/**
 * This class represents a time limiter
 */
public class TimeLimiter {
    private long timeLimitInMs;//Time limit
    private boolean timeEnded;//Did the time limit reached its limit?
    private Timer timer;
    /**
     * The constructor
     * @param timeLimitInMs - The time limit in ms
     */
    public TimeLimiter(long timeLimitInMs) {
        this.timeLimitInMs = timeLimitInMs;
        this.timeEnded = false;
    }

    /**
     * Did the time has reached its limit?
     * @return
     */
    public boolean isTimeEnded() {
        return timeEnded;
    }

    /**
     *
     */
    public void start()
    {
       this.timer = new Timer();

       timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeEnded = true;
                        System.out.println(String.format("The task has reached its time limit (%d)", timeLimitInMs));
                    }
                },
                this.timeLimitInMs
        );
    }

    public void stop()
    {
       this.timer.cancel();
    }
}
