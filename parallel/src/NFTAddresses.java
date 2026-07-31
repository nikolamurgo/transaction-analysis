import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NFTAddresses {

    public static Set<String> nftAddresses = ConcurrentHashMap.newKeySet();


    public static void addNFTAddress(String address) {
        // skip blacklisted addresses
        if (BlacklistReader.blacklistedAddresses.contains(address)) {
            return;
        }
        nftAddresses.add(address);
    }

    public static int getSize() {
        return nftAddresses.size();
    }

}
