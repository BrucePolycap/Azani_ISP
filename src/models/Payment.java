package models;

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int institutionId;
    private double amount;
    private String paymentMethod;
    private LocalDate paymentDate;

    public Payment() {
    }

    public Payment(int institutionId, double amount, String paymentMethod, LocalDate paymentDate) {
        this.institutionId = institutionId;
        this.amount = amount;
        this. paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {return paymentId;}
    public void setPaymentId(int paymentId) {this.paymentId = paymentId;}

    public int getInstitutionId() { return institutionId; }
    public void setInstitutionId(int institutionId) { this.institutionId = institutionId; }
    
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount; }

    public String getPaymentMethod() {return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod; }
    
    public LocalDate paymentDate() {return paymentDate;}
    public void setPaymentDate(LocalDate paymentDate) {this.paymentDate = paymentDate; }

    @Override 
    public String toString() {
        return "Payment{" +
                "institutionId=" + institutionId +
                ", amount" + amount +
                ", method" + paymentMethod +
                ", date" + paymentDate +
                "}";
    }
    
}
