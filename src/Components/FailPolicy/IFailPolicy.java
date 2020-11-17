package Components.FailPolicy;

import Components.Node;
import Components.Prefix;
import Components.Agent;

import java.util.Map;

/**
 * This interface will determine the failure policy for the problem
 */
public interface IFailPolicy {

    public boolean isFinishedAfterFailedIteration();
    public boolean didTheIterationFail();
    public Map<Agent,Prefix> determineSolution(Map<Agent, Node> currentPaths, int prefixSize);
    public void setDidTheIterationFail(boolean stat);
}
