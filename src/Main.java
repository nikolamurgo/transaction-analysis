import utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger.info("Application started.");
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

        Long endTime = System.currentTimeMillis();
        Logger.info("Total time: " + (endTime - startTime) * 0.001 + " seconds");

        Logger.info("Application ended.");


    }
}