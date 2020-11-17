package Components.FailPolicy;

import Components.Prefix;
import Components.Agent;

import java.util.Map;

public class FailAfterIterationFailure extends AbstractFailPolicy {

    public FailAfterIterationFailure(Map<Agent, Prefix> currentPaths,int prefixSize) {
        super(currentPaths,prefixSize);
    }

    @Override
    public boolean isFinishedAfterFailedIteration() {
        return true;
    }

    @Override
    public Map<Agent,Prefix> determineSolution() {
        return null;
    }
}
