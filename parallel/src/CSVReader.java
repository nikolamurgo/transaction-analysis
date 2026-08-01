import utils.Logger;
import utils.ParallelLineReader;

import java.util.function.Consumer;

public class CSVReader {


    public void readETN(String filePath, EtnGraph etnGraph) throws InterruptedException {
        int workerCount = Runtime.getRuntime().availableProcessors();
        ParallelLineReader.process(filePath, workerCount, false, new EtnLineHandler(etnGraph));
    }

    // parses 1 etn transaction line and adds it to the graph
    private static class EtnLineHandler implements Consumer<String> {
        private final EtnGraph etnGraph;

        EtnLineHandler(EtnGraph etnGraph) {
            this.etnGraph = etnGraph;
        }

        public void accept(String line) {
            int col = 0;
            int start = 0;
            String sender = null;
            String receiver = null;

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == ',') {
                    if (col == 5) sender = line.substring(start, i);
                    if (col == 6) receiver = line.substring(start, i);
                    col++;
                    start = i + 1;
                    if (col > 6) break;
                }
            }

            etnGraph.addTransaction(sender, receiver);
        }
    }


    // read the boredapeyachclub csv
    public void readNFTfile(String filePath) throws InterruptedException {
        int workerCount = Runtime.getRuntime().availableProcessors();
        ParallelLineReader.process(filePath, workerCount, true, new NftLineHandler());
        Logger.info("NFT addresses loaded. Count: " + NFTAddresses.nftAddresses.size());
    }

    // parses 1 nft transfer line and adds both addresses to the nft address set
    private static class NftLineHandler implements Consumer<String> {
        public void accept(String line) {
            String[] parts = line.split(",");
            String sender = parts[4].trim();
            String receiver = parts[5].trim();
            NFTAddresses.addNFTAddress(sender);
            NFTAddresses.addNFTAddress(receiver);
        }
    }

}
