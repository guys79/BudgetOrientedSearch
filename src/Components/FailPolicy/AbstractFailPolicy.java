package Components.FailPolicy;

import Components.Prefix;
import Components.Agent;

import java.util.Map;

public abstract class AbstractFailPolicy implements IFailPolicy {
    protected boolean didTheIterationFail;
    protected Map<Agent,Prefix> currentPaths;
    protected int prefixSize;


    public AbstractFailPolicy(Map<Agent, Prefix> currentPaths,int prefixSize)
    {
        this.prefixSize = prefixSize;
        this.currentPaths = currentPaths;
        didTheIterationFail = false;
    }

    @Override
    public boolean didTheIterationFail() {
        return didTheIterationFail;
    }

    public void setDidTheIterationFail(boolean didTheIterationFail) {
        this.didTheIterationFail = didTheIterationFail;
    }
}
