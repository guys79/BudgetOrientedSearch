package Components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible to check a solution for a single iteration
 * This class will use the Singleton design pattern
 */

public class SolutionChecker {

    public static SolutionChecker instance;//The instance

    /**
     * The empty constructor
     */
    private SolutionChecker()
    {

    }

    /**
     * This function will return the instance
     * @return - The instance
     */
    public static SolutionChecker getInstance()
    {
        if(instance == null)
            instance = new SolutionChecker();
        return instance;
    }

    /**
     * This function will check the given solution
     * @param prefixes - The given solution (a set of prefixes)
     * @return - True IFF the solution is valid
     */
    public boolean checkSolution(Set<Prefix> prefixes)
    {

        int prefixSize = Problem.getInstance().getPrefix();
        int numOfAgent = Problem.getInstance().getNumOfAgents();

        return checkSize(prefixes,prefixSize) && checkPrefixesValidation(prefixes)&& checkCollisions(prefixes,prefixSize,numOfAgent) && checkSwipes(prefixes,prefixSize);
    }

    /**
     * This function will check thje validation of all of the prefixes in the set
     * @param prefixes - The prefixes
     * @return - True if all of the prefixes are valid
     */
    private boolean checkPrefixesValidation(Set<Prefix> prefixes)
    {
        for(Prefix prefix:prefixes)
        {
            if(!checkSinglePrefixValidation(prefix))
                return false;
        }
        return true;
    }

    /**
     * This function will check if the prefix is valid
     * @param prefix - The given prefix
     * @return - True IFF the prefix is valid
     */
    private boolean checkSinglePrefixValidation(Prefix prefix)
    {
        if(prefix == null) {
            System.out.println("one of the prefixes is null");
            return false;
        }
        int length = prefix.getSize();
        Node prev = prefix.getNodeAt(0);
        Node next;

        for(int i=0;i<length-1;i++)
        {
            next = prefix.getNodeAt(i+1);
            if(!prev.isNeighbor(next))
            {
                System.out.println("Invalid prefix for agent "+prefix.getAgent().getId()+" "+prev+" and "+next+" are not neighbors");
                return false;
            }
            prev = next;

        }
        return true;
    }

    /**
     * This fnction will check the validation of the sizes of the prefixes
     * @param prefixes - The size of the prefixes
     * @param prefixSize - The prefix size
     * @return - True if there prefixes' sizes are valid
     */
    private boolean checkSize(Set<Prefix> prefixes,int prefixSize)
    {
        //Check if the prefix size is as defined
        for(Prefix prefix : prefixes)
        {
            if(prefix.getSize()!=prefixSize) {
                System.out.println("Wrong prefix size");
                return false;
            }
        }
        return true;
    }

    /**
     * This function will check collisions between agents
     * @param prefixes - The prefixes
     * @param prefixSize - The prefixes' size
     * @param numOfAgent - The number of agents
     * @return - True if there are no collisions
     */
    private boolean checkCollisions(Set<Prefix> prefixes,int prefixSize,int numOfAgent)
    {
        Set<Node> nodes;

        //In each time slot
        for(int i=0;i<prefixSize;i++)
        {
            nodes = new HashSet<>();
            for(Prefix prefix : prefixes)
            {
                nodes.add(prefix.getNodeAt(i));
            }

            //check that there is no duplicate
            if(numOfAgent!=nodes.size()) {
                for(Prefix prefix : prefixes)
                {
                    if(nodes.contains(prefix.getNodeAt(i))) {
                        System.out.println("Two agents collided at " + prefix.getNodeAt(i));
                        return false;
                    }
                    nodes.add(prefix.getNodeAt(i));
                }

                return false;
            }
        }
        return true;
    }

    /**
     * This function will check that there are no swipes
     * @param prefixes - The prefixes
     * @param prefixSize - The prefixes' size
     * @return - True if there are no swipes
     */
    private boolean checkSwipes(Set<Prefix> prefixes,int prefixSize)
    {
        Map<Node,Agent> nodes;
        Map<Node,Agent> newNodes;
        nodes = new HashMap<>();
        for(Prefix prefix : prefixes)
        {
            nodes.put(prefix.getNodeAt(0),prefix.getAgent());
        }

        Node node;
        Map<Agent,Agent> swappedAgents;//Key - The agent that goes into the prev location of the other agent. value - the other agent

        Agent oldAgent,newAgent;
        //For each time slot
        for(int i=1;i<prefixSize;i++)
        {
            swappedAgents = new HashMap<>();
            newNodes = new HashMap<>();

            //For each prefix
            for(Prefix prefix : prefixes)
            {
                node = prefix.getNodeAt(i);
                newAgent = prefix.getAgent();

                //If the node was occupied by an agent in the last iteration
                if(nodes.containsKey(node))
                {
                    //Get the last agent
                    oldAgent = nodes.get(node);

                    //If the agents are two different agents (eliminates the case where the agent stays in the same node)
                    if(!oldAgent.equals(newAgent)) {
                        //Check if the old agent was in some other agent's last node
                        if (swappedAgents.containsKey(oldAgent)) {
                            //If the old agent is now in the new agent's last node (and opposite)
                            if (swappedAgents.get(oldAgent).equals(newAgent)) {
                                System.out.println("Agent "+newAgent.getId() +" and agent "+oldAgent.getId()+" have swapped. They have both been in "+node +" and "+prefix.getNodeAt(i-1));
                                return false;
                            }
                        }
                        swappedAgents.put(newAgent, oldAgent);
                    }
                }
                newNodes.put(node,newAgent);
            }
            nodes = newNodes;


        }
        return true;
    }
}
