package Components.FailPolicy;

import Components.Node;
import Components.Prefix;
import Components.Agent;

import java.util.Map;

/**
 * This interface will determine the failure policy for the problem
 */
public interface IFailPolicy {

    /**
     * This function will return true if the policy determines that the solver finished his attempts to solve the problem after failure
     * @return - True if the policy determines that the solver finished his attempts to solve the problem after failure
     */
    public boolean isFinishedAfterFailedIteration();

    /**
     * This function will return true IFF the last iteration ended up in a failure
     * @return - True IFF the last iteration ended up in a failure
     */
    public boolean didTheIterationFail();

    /**
     * This function will return the prefix of the agents in case of a failed attempt
     * @param currentLocations - The current locations of all agents
     * @param prefixSize - The prefix size
     * @return
     */
    public Map<Agent,Prefix> determineSolution(Map<Agent, Node> currentLocations, int prefixSize);

    /**
     * This function will set the 'didTheIterationFail' variable
     * @param stat - The status of the last iteration
     */
    public void setDidTheIterationFail(boolean stat);
}
