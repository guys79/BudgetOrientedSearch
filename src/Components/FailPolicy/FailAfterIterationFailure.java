package Components.FailPolicy;

import Components.Node;
import Components.Prefix;
import Components.Agent;

import java.util.Map;

public class FailAfterIterationFailure extends AbstractFailPolicy {

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
