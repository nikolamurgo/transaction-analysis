import java.util.HashMap;
import java.util.Iterator;
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

            Iterator<String> senderIt = linkabilityAdjacencyList.keySet().iterator();
            while (senderIt.hasNext()) {
                String sender = senderIt.next();
                ConcurrentHashMap<String, Integer> receivers = linkabilityAdjacencyList.get(sender);

                Iterator<String> receiverIt = receivers.keySet().iterator();
                while (receiverIt.hasNext()) {
                    String receiver = receiverIt.next();
                    int weight = receivers.get(receiver);
                    pw.println(sender + "," + receiver + "," + weight);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void printWeightCounts() {
        HashMap<Integer, Integer> weightCounts = new HashMap<>();

        Iterator<ConcurrentHashMap<String, Integer>> outerIt = linkabilityAdjacencyList.values().iterator();
        while (outerIt.hasNext()) {
            ConcurrentHashMap<String, Integer> receivers = outerIt.next();

            Iterator<Integer> weightIt = receivers.values().iterator();
            while (weightIt.hasNext()) {
                int weight = weightIt.next();
                weightCounts.put(weight, weightCounts.getOrDefault(weight, 0) + 1);
            }
        }

        Iterator<Integer> keyIt = weightCounts.keySet().iterator();
        while (keyIt.hasNext()) {
            Integer weight = keyIt.next();
            System.out.println("Weight " + weight + ": " + weightCounts.get(weight));
        }
    }

}
