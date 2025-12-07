import utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger.info("Application started.");
        Long startTime = System.currentTimeMillis();

        // load the blacklisted addresses into a hashset
        BlacklistReader blacklistReader = new BlacklistReader();
        blacklistReader.readBlacklist();
        Logger.info("Blacklisted addresses loaded. "+"Count: " + BlacklistReader.blacklistedAddresses.size());

        // initialize the csv reader and the etnGraph
        CSVReader csvReader = new CSVReader();
        EtnGraph etnGraph = new EtnGraph();


        Logger.info("Loading EtnGraph...");
        // read the etn csv, add addresses to adjacency list, skip blacklisted addresses
        csvReader.readETN("data/prog3ETNsample.csv", etnGraph);


        Long endTime = System.currentTimeMillis();
        Logger.info("Total time: " + (endTime - startTime) * 0.001 + " seconds");
        Logger.info("Number of nodes in adj list: "+ etnGraph.getNumberOfNodes());

        Logger.info("Application ended.");


    }
}