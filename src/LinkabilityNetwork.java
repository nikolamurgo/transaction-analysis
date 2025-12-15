import java.util.HashMap;

public class LinkabilityNetwork {

    // sender -> (receiver -> weight)
    public static HashMap<String, HashMap<String, Integer>> linkabilityAdjacencyList;

    public LinkabilityNetwork() {
        linkabilityAdjacencyList = new HashMap<>();
    }


    public static void addEdge(String sender, String receiver, int weight){
        // directed weighted graph, with mult edges and no self loops
        HashMap<String, Integer> receivers = linkabilityAdjacencyList.get(sender);
        if (receivers == null) {
            receivers = new HashMap<>();
            linkabilityAdjacencyList.put(sender, receivers);
        }
        receivers.put(receiver, weight);

    }
}
