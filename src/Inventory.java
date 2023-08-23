public class Inventory {

    private int totalRocks, totalWood;
    public static int returnedRocks;

    public Inventory() {
        this.totalRocks = 0;
        this.totalWood = 0;
        returnedRocks = 0;
    }

    public int getTotalRocks() {
        this.totalRocks += returnedRocks;
        returnedRocks -= returnedRocks;
        return totalRocks;
    }

    public void setTotalRocks(int totalRocks) {
        this.totalRocks += totalRocks;
    }

    public int getTotalWood() {
        return totalWood;
    }

    public void setTotalWood(int totalWood) {
        this.totalWood += totalWood;
    }
}
