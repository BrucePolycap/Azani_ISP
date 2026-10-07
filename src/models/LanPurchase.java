package models;

public class LanPurchase {
    private int lanId;
    private int institutionId;
    private int numNodes;
    private double totalCost;

    public LanPurchase() {
    }

    public LanPurchase(int institutionId, int numNodes) {
        this.institutionId = institutionId;
        this.numNodes = numNodes;
        this.totalCost = calculateLanCost(numNodes);
    }

    public static double calculateLanCost(int numNodes) {
        if (numNodes >= 2  && numNodes <= 10)  return 10000;
        if (numNodes >= 11 && numNodes <= 20)  return 20000;
        if (numNodes >= 21 && numNodes <= 40)  return 30000;
        if (numNodes >= 41 && numNodes <= 100) return 40000;
        return 0;
    }

    public int getLanId() {return lanId; }
    public void setLanId(int lanId) {this.lanId = lanId; }

    public int getInstitutionId() {return institutionId; }
    public void setInstitutionId(int institutionId) {this.institutionId = institutionId; }

    public int getNumNodes() {return numNodes; }
    public void setNumNodes(int numNodes) {this.numNodes = numNodes; }

    public double getTotalCost() {return totalCost; }
    public void setTotalCost(double totalCost) {this.totalCost = totalCost; }

    @Override 
    public String toString() {
        return "LanPurchase{" +
                "institutionId=" + institutionId +
                ", numNodes=" + numNodes +
                ", totalCost=" + totalCost +
                "}";
    }
}
