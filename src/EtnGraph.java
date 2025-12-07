import java.util.*;

public class EtnGraph {

    // adjacency list sender -> set of receivers
    public static HashMap<String, HashSet<String>> adjacencyList;

    // constructor
    public EtnGraph() {
        adjacencyList = new HashMap<>();
    }

    // add sender-receiver pair to adjacency list
    public void addTransaction(String sender, String receiver) {
        if(BlacklistReader.blacklistedAddresses.contains(sender) || BlacklistReader.blacklistedAddresses.contains(receiver)) {
            return;
        }

        HashSet<String> receivers = adjacencyList.get(sender);
        if (receivers == null) {
            receivers = new HashSet<>();
            adjacencyList.put(sender, receivers);
        }
        receivers.add(receiver);
    }

    public int getNumberOfNodes() {
        return adjacencyList.size();
    }


}
