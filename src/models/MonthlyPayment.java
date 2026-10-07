package models;

import java.time.LocalDate;

public class MonthlyPayment {
    private int monthlyId;
    private int institutionId;
    private int bandwidth;
    private double amount;
    private double discount;
    private double fine;
    private double reconnectionFee;
    private String month;
    private int year;
    private String paymentMethod;
    private boolean isPaid;
    private boolean isUpgrade;
    private LocalDate paymentDate;

    public MonthlyPayment() {
    }

    public MonthlyPayment(int institutionId, int bandwidth, double amount, String month, int year) {
        this.institutionId = institutionId;
        this.bandwidth = bandwidth;
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.discount = 0;
        this.fine = 0;
        this.reconnectionFee = 0;
        this.isPaid = false;
        this.isUpgrade = false;
    }

    // Getters and Setters
    public int getMonthlyId() { return monthlyId; }
    public void setMonthlyId(int monthlyId) { this.monthlyId = monthlyId; }

    public int getInstitutionId() { return institutionId; }
    public void setInstitutionId(int institutionId) { this.institutionId = institutionId; }

    public int getBandwidth() { return bandwidth; }
    public void setBandwidth(int bandwidth) { this.bandwidth = bandwidth; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }

    public double getReconnectionFee() { return reconnectionFee; }
    public void setReconnectionFee(double reconnectionFee) { this.reconnectionFee = reconnectionFee; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    public boolean isUpgrade() { return isUpgrade; }
    public void setUpgrade(boolean upgrade) { isUpgrade = upgrade; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    // Helper method: total due = amount + fine + reconnection - discount
    public double getTotalDue() {
        return (amount + fine + reconnectionFee) - discount;
    }

    @Override
    public String toString() {
        return "MonthlyPayment{" +
                "institutionId=" + institutionId +
                ", bandwidth=" + bandwidth +
                ", amount=" + amount +
                ", month='" + month + '\'' +
                ", year=" + year +
                ", isPaid=" + isPaid +
                '}';
    }
}