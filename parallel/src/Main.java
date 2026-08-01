import utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        int MAX_DEPTH = 3;

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


        Logger.info("Loading EtnGraph and NFT addresses...");
        // read the etn csv and the nft csv at the same time, they write to independent structures
        EtnLoaderTask etnTask = new EtnLoaderTask(csvReader, etnGraph, "data/prog3ETNsample.csv");
        NftLoaderTask nftTask = new NftLoaderTask(csvReader, "data/boredapeyachtclub.csv");

        Thread etnThread = new Thread(etnTask);
        Thread nftThread = new Thread(nftTask);

        etnThread.start();
        nftThread.start();

        etnThread.join();
        nftThread.join();

        if (etnTask.getError() != null) throw etnTask.getError();
        if (nftTask.getError() != null) throw nftTask.getError();

        Logger.debug("Number of nodes in adj list: "+ etnGraph.getNumberOfNodes());

        Logger.info("Building Linkability Network...");
        LinkabilityNetwork linkNet = new LinkabilityNetwork();
        etnGraph.buildLinkabilityNetwork(linkNet,MAX_DEPTH);
        Logger.info("Linkability Network built.");

        LinkabilityNetwork.exportToCSV("data/linkability.csv");
        Logger.info("Linkability Network exported to CSV.");

        Logger.info("Weight counts in Linkability Network:");
        LinkabilityNetwork.printWeightCounts();


        Long endTime = System.currentTimeMillis();
        Logger.info("Total time: " + (endTime - startTime) * 0.001 + " seconds");

        Logger.info("Application ended.");


    }

    // runs csvReader.readETN on its own thread, captures any InterruptedException to rethrow after join
    private static class EtnLoaderTask implements Runnable {
        private final CSVReader csvReader;
        private final EtnGraph etnGraph;
        private final String filePath;
        private volatile InterruptedException error;

        EtnLoaderTask(CSVReader csvReader, EtnGraph etnGraph, String filePath) {
            this.csvReader = csvReader;
            this.etnGraph = etnGraph;
            this.filePath = filePath;
        }

        public void run() {
            try {
                csvReader.readETN(filePath, etnGraph);
            } catch (InterruptedException e) {
                error = e;
                Thread.currentThread().interrupt();
            }
        }

        InterruptedException getError() {
            return error;
        }
    }

    // runs csvReader.readNFTfile on its own thread, captures any InterruptedException to rethrow after join
    private static class NftLoaderTask implements Runnable {
        private final CSVReader csvReader;
        private final String filePath;
        private volatile InterruptedException error;

        NftLoaderTask(CSVReader csvReader, String filePath) {
            this.csvReader = csvReader;
            this.filePath = filePath;
        }

        public void run() {
            try {
                csvReader.readNFTfile(filePath);
            } catch (InterruptedException e) {
                error = e;
                Thread.currentThread().interrupt();
            }
        }

        InterruptedException getError() {
            return error;
        }
    }
}