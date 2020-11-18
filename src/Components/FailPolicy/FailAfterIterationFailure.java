package Components.FailPolicy;

import Components.Node;
import Components.Prefix;
import Components.Agent;

import java.util.Map;

/**
 * This function represents a fail policy where the solver will not attempt a second attempt in case of a failure
 */
public class FailAfterIterationFailure extends AbstractFailPolicy {

    /**
     * The constructor
     */
    public FailAfterIterationFailure() {
        super();
    }

    @Override
    public boolean isFinishedAfterFailedIteration() {
        return true;
    }

    @Override
    public Map<Agent,Prefix> determineSolution(Map<Agent, Node> currentPaths, int prefixSize) {
        return null;
    }
}
