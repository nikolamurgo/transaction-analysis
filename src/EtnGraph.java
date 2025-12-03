import java.util.*;

public class EtnGraph {

    // adjacency list sender -> list of receivers
    private HashMap<String, ArrayList<String>> adjacencyList;


    // constructor
    public EtnGraph() {
        adjacencyList = new HashMap<>(200);
    }

    // add sender-receiver pair to adjacency list
    // TODO: fix function to avoid duplicates
    public void addTransaction(String sender, String receiver) {
        ArrayList<String> receivers = adjacencyList.get(sender);
        if (receivers == null) {
            receivers = new ArrayList<>(2);
            adjacencyList.put(sender, receivers);
        }
        receivers.add(receiver);
    }

    public int getNumberOfNodes() {
        return adjacencyList.size();
    }


    // TODO : remove blacklisted nodes from the adjacency list


}
