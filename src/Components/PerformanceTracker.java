package Components;

/**
 * This class will track after the algorithm's performance
 * This class uses Singleton design pattern
 */
public class PerformanceTracker {

    private static PerformanceTracker instance;
    private int numberOFIteration;
    private long preCompute;
    private long overAllSearch;
    private int numOfPreComputedAgents;

    /**
     * The constructor of the class
     */
    private PerformanceTracker()
    {
        this.numberOFIteration = 0;
        this.numOfPreComputedAgents = 0;
        this.preCompute = 0;
        this.overAllSearch = 0;
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

    public void addPreCompute(long preCompute) {
        this.preCompute += preCompute;
    }

    public void setOverAllSearch(long overAllSearch) {
        this.overAllSearch = overAllSearch;
    }

    public long getOverAllSearch() {
        return overAllSearch;
    }

    public long getPreCompute() {
        return preCompute;
    }

    public long getSearchTimeNeto() {
        return overAllSearch - preCompute;
    }

    public void addNumOfPreComputedAgents() {
        this.numOfPreComputedAgents += 1;
    }

    public int getNumOfPreComputedAgents() {
        return numOfPreComputedAgents;
    }
}
