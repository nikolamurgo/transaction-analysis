import java.util.HashSet;

public class NFTAddresses {

    public static HashSet<String> nftAddresses = new HashSet<>();
    static int c1 = 0; // temporray to visualize how many addresses are not concerned
    static int c2 = 0; // same for duplicates


    public static void addNFTAddress(String address) {
        // skip blacklisted addresses
        if (BlacklistReader.blacklistedAddresses.contains(address)) {
            c1++;
            return;
        }
    // skip duplicates, ONLY FOR DEVELOPMENT PURPOSE to count them, TODO delete after bcs they are automat deleted
        if(nftAddresses.contains(address)){
            c2++;
            return;
        }
        nftAddresses.add(address);
    }

    public static int getRemovedCount() {
        return c1;
    }

    // for development purpose TODO: delete after
    public static int getDuplicateCount() {
        return c2;
    }

}
