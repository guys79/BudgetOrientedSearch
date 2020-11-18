package Components.FailPolicy;


/**
 * This interface represents am abstract Fail policy - what to do in case of a failed iteration
 */
public abstract class AbstractFailPolicy implements IFailPolicy {
    protected boolean didTheIterationFail; // True IFF the last iteration has failed

    /**
     * The constructor of the class
     */
    public AbstractFailPolicy()
    {

        didTheIterationFail = false;
    }

    @Override
    public boolean didTheIterationFail() {
        return didTheIterationFail;
    }

    @Override
    public void setDidTheIterationFail(boolean didTheIterationFail) {
        this.didTheIterationFail = didTheIterationFail;
    }
}
