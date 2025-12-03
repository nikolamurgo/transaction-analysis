import utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger.info("Application started.");

        // load the etn
        EtnGraph etnGraph = new EtnGraph();

        CSVReader csvReader = new CSVReader();


        Long startTime = System.currentTimeMillis();
        // read the etn csv
        csvReader.readETN("data/prog3ETNsample.csv", etnGraph);

        Long endTime = System.currentTimeMillis();
        Logger.info("Total time: " + (endTime - startTime) * 0.001 + " seconds");
        Logger.info("Number of nodes in adj list: "+ etnGraph.getNumberOfNodes());



        Logger.info("Application ended.");


    }
}