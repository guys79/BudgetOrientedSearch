package Components;

/**
 * This class will track after the algorithm's performance
 * This class uses Singleton design pattern
 */
public class PerformanceTracker {

    private static PerformanceTracker instance;
    private int numberOFIteration;

    /**
     * The constructor of the class
     */
    private PerformanceTracker()
    {
        this.numberOFIteration = 0;
    }

    /**
     * This function will return the instance
     * @return - The instance
     */
    public static PerformanceTracker getInstance() {
        if(instance == null)
            instance = new PerformanceTracker();
        return instance;
    }

    /**
     * This function will add 1 to the number of iterations
     */
    public void addIteration()
    {
        this.numberOFIteration++;
    }

    /**
     * This function will return the number of iterations
     * @return - The number of iterations
     */
    public int getNumberOFIteration() {
        return numberOFIteration;
    }
}
