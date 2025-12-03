import utils.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

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

    // TODO: read blacklist files


    // TODO: read nft boredapeyachtclub file


}
