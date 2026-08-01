import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EtnGraph {

    // adjacency list sender -> set of receivers
    public ConcurrentHashMap<String, Set<String>> adjacencyList;

    // constructor
    public EtnGraph() {
        adjacencyList = new ConcurrentHashMap<>();
    }

    // add sender-receiver pair to adjacency list
    public void addTransaction(String sender, String receiver) {
        if(BlacklistReader.blacklistedAddresses.contains(sender) || BlacklistReader.blacklistedAddresses.contains(receiver)) {
            return;
        }

        Set<String> receivers = adjacencyList.get(sender);
        if (receivers == null) {
            Set<String> newSet = ConcurrentHashMap.newKeySet();
            Set<String> existing = adjacencyList.putIfAbsent(sender, newSet);
            receivers = (existing != null) ? existing : newSet;
        }
        receivers.add(receiver);
    }

    // build linkability network using BFS up to maxDepth, 1 bfs per nft adr
    // addresses split into groups so multiple threads run bfs at once
    public void buildLinkabilityNetwork(LinkabilityNetwork linkNet, int maxDepth) throws InterruptedException {

        int threadCount = Runtime.getRuntime().availableProcessors();
        List<List<String>> groups = splitIntoGroups(new ArrayList<>(NFTAddresses.nftAddresses), threadCount);

        Thread[] workers = new Thread[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            workers[i] = new Thread(new BfsWorker(this, groups.get(i), maxDepth));
        }

        for (int i = 0; i < workers.length; i++) {
            workers[i].start();
        }
        for (int i = 0; i < workers.length; i++) {
            workers[i].join();
        }
    }

    // splits addresses into up to groupCount  equal sublists
    private static List<List<String>> splitIntoGroups(List<String> addresses, int groupCount) {
        List<List<String>> groups = new ArrayList<>();
        int size = addresses.size();
        if (size == 0) return groups;

        int groupSize = (int) Math.ceil((double) size / groupCount);
        int start = 0;
        while (start < size) {
            int end = Math.min(size, start + groupSize);
            groups.add(addresses.subList(start, end));
            start = end;
        }
        return groups;
    }

    private void bfsFrom(String start, int maxDepth) {
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

            Set<String> neighbors = adjacencyList.get(current);
            if (neighbors == null) continue;

            Iterator<String> neighborIt = neighbors.iterator();
            while (neighborIt.hasNext()) {
                String neighbor = neighborIt.next();

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    distance.put(neighbor, currentDistance + 1);

                    if (NFTAddresses.nftAddresses.contains(neighbor) && !neighbor.equals(start)) {
                        LinkabilityNetwork.addEdge(start, neighbor, currentDistance + 1);
                    }
                }
            }
        }
    }

    // runs BFS for one group of NFT addresses on its own thread
    private static class BfsWorker implements Runnable {
        private final EtnGraph etnGraph;
        private final List<String> starts;
        private final int maxDepth;

        BfsWorker(EtnGraph etnGraph, List<String> starts, int maxDepth) {
            this.etnGraph = etnGraph;
            this.starts = starts;
            this.maxDepth = maxDepth;
        }

        public void run() {
            for (int i = 0; i < starts.size(); i++) {
                etnGraph.bfsFrom(starts.get(i), maxDepth);
            }
        }
    }


    public int getNumberOfNodes() {
        return adjacencyList.size();
    }


}
