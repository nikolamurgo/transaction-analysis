import java.util.HashSet;

public class NFTAddresses {

    public static HashSet<String> nftAddresses = new HashSet<>();


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
