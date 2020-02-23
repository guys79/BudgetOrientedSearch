import Components.Node;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int [] coordinates = {4,4,4};
        int [] size = {5,100,1000};

        Node node = new Node(coordinates);
        Set<Node> neighbors= node.expend(size);

        for(Node neigh :neighbors)
        {
            System.out.println(neigh);
        }
    }
}
