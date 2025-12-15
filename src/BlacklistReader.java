import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import utils.Logger;

public class BlacklistReader {

    private static final File folder = new File("data/blacklist");
    // store in an array the json files from the blacklisted folder.
    private static final File[] files = folder.listFiles();

    // store the blacklisted addresses in an hashset
    public static final HashSet<String> blacklistedAddresses = new HashSet<>();


    public void readBlacklist(){
        try {
            for (int i=0; i <= files.length-1; i++){
                BufferedReader br = new BufferedReader(new FileReader(files[i]));
                String node;
                while((node = br.readLine()) != null){
                    node = node.trim(); // remove whitespaces
                    if(node.startsWith("[") || node.startsWith("]")){
                        continue;
                    }
                    node = node.replace("\"", "").replace(",", "").trim();
                    blacklistedAddresses.add(node);
                }
                br.close();
            }
        }catch (Exception e){
            Logger.error(e.getMessage());
        }

    }

    public int getBlacklistSize(){
        return blacklistedAddresses.size();
    }

}
