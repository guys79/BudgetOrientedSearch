package Components;

import java.util.List;

/**
 * This class represent prefix of nodes
 */
public class Prefix {

    private Agent agent;//The agent whom the prefix belongs to
    private List<Node> prefix;//The prefix

    /**
     * The constructor of the prefix
     * @param prefix - The given prefix
     * @param agent  - The agent
     */
    public Prefix(List<Node> prefix,Agent agent)
    {
        this.prefix = prefix;
        this.agent = agent;
    }

    /**
     * This function will return the size of the node
     * @return - The size of the node
     */
    public int getSize()
    {
        return this.prefix.size();
    }

    /**
     * This function will return the I'th node in the prefix
     * @param index - The given index (i)
     * @return - The I'th node
     */
    public Node getNodeAt(int index)
    {
        if(index>=getSize())
            throw new UnsupportedOperationException("The index is bigger or equal to the prefix's length");
        return this.prefix.get(index);
    }

    /**
     * This function will return the agent whom this prefix belongs to
     * @return - The agent whom this prefix belongs to
     */
    public Agent getAgent() {
        return agent;
    }

    @Override
    public String toString() {
        String str = "{";
        for(int i=0;i<this.prefix.size();i++)
        {
            str+=this.prefix.get(i)+",";
        }
        str=str.substring(0,str.length()-1)+"}";
        return str;
    }
}
