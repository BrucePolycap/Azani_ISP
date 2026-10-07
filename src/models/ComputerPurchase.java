package models;

public class ComputerPurchase {
    private int purchaseId;
    private int institutionId;
    private int quantity;
    private int unitPrice;
    private double totalPrice;

    public ComputerPurchase() {
    }

    public ComputerPurchase(int institutionId, int quantity, int unitPrice, double totalPrice) {
        this.institutionId = institutionId;
        this.quantity = quantity;
        this.unitPrice = 40000;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public int getPurchaseId() { return purchaseId; }
    public void setPurchaseId(int purchaseId) { this.purchaseId = purchaseId; }

    public int getInstitutionId() { return institutionId; }
    public void setInstitutionId(int institutionId) { this.institutionId = institutionId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getUnitPrice() { return unitPrice; }
    public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

        @Override
    public String toString() {
        return "ComputerPurchase{" +
                "institutionId=" + institutionId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
    
}
