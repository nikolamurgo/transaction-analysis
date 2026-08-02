import mpi.MPI;
import mpi.Status;
import utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int MAX_DEPTH = 3;

        Logger.info("Application started. Rank " + rank + " of " + size + ".");
        Long startTime = System.currentTimeMillis();

        // load the blacklisted addresses into a hashset
        Logger.info("Loading blacklisted addresses...");
        BlacklistReader blacklistReader = new BlacklistReader();
        blacklistReader.readBlacklist();
        Logger.debug("Blacklisted addresses loaded. "+"Count: " + blacklistReader.getBlacklistSize());

        // initialize the csv reader and the etnGraph
        CSVReader csvReader = new CSVReader();
        EtnGraph etnGraph = new EtnGraph();


        Logger.info("Loading EtnGraph...");
        // read the etn csv, add addresses to adjacency list, skip blacklisted addresses
        csvReader.readETN("data/prog3ETNsample.csv", etnGraph);
        Logger.debug("Number of nodes in adj list: "+ etnGraph.getNumberOfNodes());


        Logger.info("Loading NFT addresses...");
        // read the boredapeyachtclub csv, add addresses to nftAddresses hashset, skip blacklisted and duplicate addresses
        csvReader.readNFTfile("data/boredapeyachtclub.csv");

        Logger.info("Building Linkability Network for this rank's address slice...");
        LinkabilityNetwork linkNet = new LinkabilityNetwork();
        java.util.List<String> localEdges = etnGraph.buildLinkabilityNetwork(linkNet, MAX_DEPTH, rank, size);
        Logger.info("Rank " + rank + " found " + localEdges.size() + " local edges.");

        // non-zero ranks send their local edges to rank 0, rank 0 receives from every other
        // rank and merges everyone's edges into one LinkabilityNetwork
        if (rank != 0) {
            String[] edgesArray = localEdges.toArray(new String[0]);
            MPI.COMM_WORLD.Send(edgesArray, 0, edgesArray.length, MPI.OBJECT, 0, 0);
            Logger.info("Rank " + rank + " sent " + edgesArray.length + " edges to rank 0.");
        } else {
            mergeEdges(localEdges);

            for (int sourceRank = 1; sourceRank < size; sourceRank++) {
                Status status = MPI.COMM_WORLD.Probe(sourceRank, 0);
                int count = status.Get_count(MPI.OBJECT);

                String[] received = new String[count];
                MPI.COMM_WORLD.Recv(received, 0, count, MPI.OBJECT, sourceRank, 0);

                mergeEdges(java.util.Arrays.asList(received));
                Logger.info("Rank 0 received " + count + " edges from rank " + sourceRank + ".");
            }
        }


        Long endTime = System.currentTimeMillis();
        Logger.info("Total time: " + (endTime - startTime) * 0.001 + " seconds");

        Logger.info("Application ended.");

        MPI.Finalize();

    }

    // parses "sender,receiver,weight" strings back into edges and adds them to linkability netwfork
    private static void mergeEdges(List<String> edges) {
        for (int i = 0; i < edges.size(); i++) {
            String[] parts = edges.get(i).split(",");
            String sender = parts[0];
            String receiver = parts[1];
            int weight = Integer.parseInt(parts[2]);
            LinkabilityNetwork.addEdge(sender, receiver, weight);
        }
    }
}