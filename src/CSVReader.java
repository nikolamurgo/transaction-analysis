import utils.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class CSVReader {


    public void readETN(String filePath, EtnGraph etnGraph) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(filePath));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String sender = values[5].intern();
                String receiver = values[6].intern();
                // use method from etngraph to add to adj list
                etnGraph.addTransaction(sender, receiver);

            }
            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // TODO: read blacklist files


    // TODO: read nft boredapeyachtclub file


}
