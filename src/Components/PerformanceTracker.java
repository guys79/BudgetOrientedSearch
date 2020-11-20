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
    private boolean complete;

    /**
     * The constructor of the class
     */
    private PerformanceTracker()
    {
        reset();
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

    /**
     * This function will add to the pre-computation time
     * @param preCompute - The amount to add
     */
    public void addPreCompute(long preCompute) {
        this.preCompute += preCompute;
    }

    /**
     * This function will set the overall search time
     * @param overAllSearch - The overall search time
     */
    public void setOverAllSearch(long overAllSearch) {
        this.overAllSearch = overAllSearch;
    }

    /**
     * This function will return the overall search time
     * @return - The overall search time
     */
    public long getOverAllSearch() {
        return overAllSearch;
    }

    /**
     * This function will return the pre-computation time
     * @return - The pre-computation time
     */
    public long getPreCompute() {
        return preCompute;
    }

    /**
     * This function will return the search time neto
     * @return - Neto search time
     */
    public long getSearchTimeNeto() {
        return overAllSearch - preCompute;
    }

    /**
     * Add one to the number of pre-computed agents
     */
    public void addNumOfPreComputedAgents() {
        this.numOfPreComputedAgents += 1;
    }

    /**
     * This function will return the number of pre - computed agents
     * @return - The number of pre - computed agents
     */
    public int getNumOfPreComputedAgents() {
        return numOfPreComputedAgents;
    }

    /**
     * This function will set the completion status
     * @param complete - The completion status
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * This function will return the completion status
     * @return - The completion status
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Thus function will reset the parameters of the class
     */
    public void reset()
    {
        this.complete = false;
        this.numberOFIteration = 0;
        this.numOfPreComputedAgents = 0;
        this.preCompute = 0;
        this.overAllSearch = 0;
    }
}
