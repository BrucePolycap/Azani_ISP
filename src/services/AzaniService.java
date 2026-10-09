package services;

import dao.*;
import models.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AzaniService {

    // Shared DAO instances
    private final InstitutionDAO institutionDAO = new InstitutionDAO();
    private final ContactPersonDAO contactDAO = new ContactPersonDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final InfrastructureDAO infraDAO = new InfrastructureDAO();
    private final ComputerPurchaseDAO computerDAO = new ComputerPurchaseDAO();
    private final LanPurchaseDAO lanDAO = new LanPurchaseDAO();
    private final MonthlyPaymentDAO monthlyDAO = new MonthlyPaymentDAO();

    
    // Total installation cost for each institution
    // = Total installation fee + computer cost + LAN cost
    
    public double getTotalInstallationCost(int institutionId) {
        double installationFee = 0;

        // Sum all installation payments for this institution
        for (Payment p : paymentDAO.getInstallationPayments(institutionId)) {
            installationFee += p.getAmount();
        }

        double computerCost = computerDAO.getTotalComputerCostByInstitution(institutionId);
        double lanCost      = lanDAO.getTotalLanCostByInstitution(institutionId);

        return installationFee + computerCost + lanCost;
    }

    
    // Cost of computers + LAN for institutions
    //             with assorted services
    
    public Map<String, Double> getAssortedServiceCost(int institutionId) {
        Map<String, Double> result = new HashMap<>();
        result.put("computers", computerDAO.getTotalComputerCostByInstitution(institutionId));
        result.put("lan",       lanDAO.getTotalLanCostByInstitution(institutionId));
        return result;
    }

    
    // Total monthly charges for upgraded internet services
    
    public double getTotalUpgradedCharges() {
        double total = 0;

        // Loop through every institution and look for is_upgrade bills
        for (Institution inst : institutionDAO.getAllInstitutions()) {
            for (MonthlyPayment mp : monthlyDAO.getByInstitution(inst.getInstitutionId())) {
                if (mp.isUpgrade()) {
                    total += mp.getTotalDue();
                }
            }
        }
        return total;
    }

    
    // Total monthly + fines + reconnection fees
    //             grouped by institution type
    
    public Map<String, Double> getChargesByCategory() {
        Map<String, Double> result = new HashMap<>();
        String[] categories = {"Primary", "Junior", "Senior", "College"};

        for (String category : categories) {
            double monthlyTotal      = 0;
            double finesTotal        = 0;
            double reconnectionTotal = 0;

            for (Institution inst : institutionDAO.getAllInstitutions()) {
                if (!inst.getType().equalsIgnoreCase(category)) continue;

                for (MonthlyPayment mp : monthlyDAO.getByInstitution(inst.getInstitutionId())) {
                    monthlyTotal      += mp.getAmount();
                    finesTotal        += mp.getFine();
                    reconnectionTotal += mp.getReconnectionFee();
                }
            }

            double grandTotal = monthlyTotal + finesTotal + reconnectionTotal;
            result.put(category, grandTotal);
        }
        return result;
    }

    // Aggregate amount per service sorted by institution
    //   For each institution:
    //   total registration + total installation + total monthly
    public Map<String, Double> getAggregateByInstitution() {
        Map<String, Double> result = new HashMap<>();

        for (Institution inst : institutionDAO.getAllInstitutions()) {
            int id = inst.getInstitutionId();

            double regTotal = 0;
            for (Payment p : paymentDAO.getRegistrationPayments(id)) regTotal += p.getAmount();

            double instTotal = 0;
            for (Payment p : paymentDAO.getInstallationPayments(id)) instTotal += p.getAmount();

            double monthlyTotal = 0;
            for (MonthlyPayment mp : monthlyDAO.getByInstitution(id)) {
                monthlyTotal += mp.getTotalDue();
            }

            double grandTotal = regTotal + instTotal + monthlyTotal;
            result.put(inst.getName(), grandTotal);
        }
        return result;
    }

    
    // List of defaulters
    
    public List<MonthlyPayment> getDefaulters() {
        return monthlyDAO.getDefaulters();
    }


    // List of institutions with disconnection issues
    public List<MonthlyPayment> getDisconnectionIssues() {
        return monthlyDAO.getDisconnectionIssues();
    }


    // Infrastructure requirements for each institution
    public List<Infrastructure> getAllInfrastructure() {
        return infraDAO.getAllInfrastructure();
    }


    // Registered institutions
    public List<Institution> getRegisteredInstitutions() {
        return institutionDAO.getAllInstitutions();
    }

    //Get all monthly bills for a single institution
    public List<MonthlyPayment> getBillsForInstitution(int institutionId) {
        return monthlyDAO.getByInstitution(institutionId);
    }
}