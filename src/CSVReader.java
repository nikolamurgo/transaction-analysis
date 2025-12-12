import utils.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CSVReader {


    public void readETN(String filePath, EtnGraph etnGraph) throws FileNotFoundException {
        // size for buffer reader 64KB, to speed up reading large files
        BufferedReader br = new BufferedReader(new java.io.FileReader(filePath), 65536);
        String line;
        try {
            while ((line = br.readLine()) != null) {
                int col = 0;
                int start = 0;
                String sender = null;
                String receiver = null;

                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ',') {
                        if (col == 5) sender = line.substring(start, i).intern();
                        if (col == 6) receiver = line.substring(start, i).intern();
                        col++;
                        start = i + 1;
                        if (col > 6) break;
                    }
                }

                etnGraph.addTransaction(sender, receiver);
            }

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    // read the boredapeyachclub csv
    public void readNFTfile(String filePath, NFTAddresses nftAddresses) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        String sender;
        String receiver;
        String[] parts;
        try {
            br.readLine(); // skip first line header

            while ((line = br.readLine()) != null) {
                parts = line.split(",");
                sender = parts[4].trim();
                receiver = parts[5].trim();
                nftAddresses.addNFTAddress(sender);
                nftAddresses.addNFTAddress(receiver);
            }
            Logger.info("NFT addresses loaded. Count: " + NFTAddresses.nftAddresses.size());
            br.close();
        }catch (Exception e){
            Logger.error(e.getMessage());
        }

    }

}
