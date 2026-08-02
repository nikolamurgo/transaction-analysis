import java.util.*;

public class EtnGraph {

    // adjacency list sender -> set of receivers
    public HashMap<String, HashSet<String>> adjacencyList;

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

    // build linkability network using BFS up to maxDepth DEFINED IN MAIN, but only for the
    // slice of NFT addresses assigned to this rank (addresses at index rank, rank+size, rank+2*size, ...)
    // addresses are sorted first so every rank computes the exact same ordering independently
    public List<String> buildLinkabilityNetwork(LinkabilityNetwork linkNet, int maxDepth, int rank, int size) {

        List<String> addresses = new ArrayList<>(NFTAddresses.nftAddresses);
        Collections.sort(addresses);

        List<String> localEdges = new ArrayList<>();

        for (int i = rank; i < addresses.size(); i += size) {
            bfsFrom(addresses.get(i), maxDepth, localEdges);
        }

        return localEdges;
    }

    // BFS from a single NFT address; discovered edges are appended as "sender,receiver,weight"
    // strings to localEdges instead of going into the shared LinkabilityNetwork
    private void bfsFrom(String start, int maxDepth, List<String> localEdges) {
        Queue<String> queue = new LinkedList<>();
        HashMap<String, Integer> distance = new HashMap<>();
        HashSet<String> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDistance = distance.get(current);

            if (currentDistance >= maxDepth) {
                continue;
            }

            HashSet<String> neighbors = adjacencyList.get(current);
            if (neighbors == null) continue;

            for (String neighbor : neighbors) {

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    distance.put(neighbor, currentDistance + 1);

                    if (NFTAddresses.nftAddresses.contains(neighbor) && !neighbor.equals(start)) {
                        localEdges.add(start + "," + neighbor + "," + (currentDistance + 1));
                    }
                }
            }
        }
    }


    public int getNumberOfNodes() {
        return adjacencyList.size();
    }


}
