import mpi.MPI;
import utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

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
        // sending these to rank 0 and exporting the merged result is added in later steps


        Long endTime = System.currentTimeMillis();
        Logger.info("Total time: " + (endTime - startTime) * 0.001 + " seconds");

        Logger.info("Application ended.");

        MPI.Finalize();

    }
}