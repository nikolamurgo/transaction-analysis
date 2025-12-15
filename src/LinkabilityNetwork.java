import java.util.HashMap;

public class LinkabilityNetwork {

    // sender -> (receiver -> weight)
    public HashMap<String, HashMap<String, Integer>> linkabilityAdjacencyList;

    public LinkabilityNetwork() {
        linkabilityAdjacencyList = new HashMap<>();
    }

    // TODO: add to adjacency list
}
