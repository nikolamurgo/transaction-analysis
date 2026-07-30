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

        for (HashMap<String, Integer> receivers : linkabilityAdjacencyList.values()) {
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
