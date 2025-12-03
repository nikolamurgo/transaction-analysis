import utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger.info("Application started.");

        // load the etn
        EtnGraph etnGraph = new EtnGraph();

        CSVReader csvReader = new CSVReader();
        // read the etn csv
        csvReader.readETN("data/prog3ETNsample.csv", etnGraph);

        Logger.info("Application ended.");
    }
}