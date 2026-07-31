import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class LinkabilityNetwork {

    // sender -> (receiver -> weight)
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> linkabilityAdjacencyList;

    public LinkabilityNetwork() {
        linkabilityAdjacencyList = new ConcurrentHashMap<>();
    }


    public static void addEdge(String sender, String receiver, int weight){
        // directed weighted graph, with mult edges and no self loops
        ConcurrentHashMap<String, Integer> receivers = linkabilityAdjacencyList.get(sender);
        if (receivers == null) {
            ConcurrentHashMap<String, Integer> newMap = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Integer> existing = linkabilityAdjacencyList.putIfAbsent(sender, newMap);
            receivers = (existing != null) ? existing : newMap;
        }
        receivers.put(receiver, weight);

    }

    public static void exportToCSV(String filePath) {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(filePath))) {

            for (String sender : linkabilityAdjacencyList.keySet()) {
                for (String receiver : linkabilityAdjacencyList.get(sender).keySet()) {
                    int weight = linkabilityAdjacencyList.get(sender).get(receiver);
                    pw.println(sender + "," + receiver + "," + weight);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void printWeightCounts() {
        HashMap<Integer, Integer> weightCounts = new HashMap<>();

        for (ConcurrentHashMap<String, Integer> receivers : linkabilityAdjacencyList.values()) {
            for (int weight : receivers.values()) {
                weightCounts.put(weight,
                        weightCounts.getOrDefault(weight, 0) + 1);
            }
        }

        for (Integer weight : weightCounts.keySet()) {
            System.out.println("Weight " + weight + ": " + weightCounts.get(weight));
        }
    }

}
