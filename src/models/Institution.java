package models;

import java.time.LocalDate;

public class Institution {
    // These variables match the columns in your 'institution' table
    private int institutionId;
    private String name;
    private String type;
    private String location;
    private boolean isReady;
    private boolean isDisconnected;
    private LocalDate dateRegistered;

    // 1. Default Constructor (empty)
    public Institution() {
    }

    // 2. Parameterized Constructor (for easy creation)
    public Institution(String name, String type, String location, boolean isReady, boolean isDisconnected, LocalDate dateRegistered) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.isReady = isReady;
        this.isDisconnected = isDisconnected;
        this.dateRegistered = dateRegistered;
    }

    // 3. Getters and Setters
    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public void setDisconnected(boolean disconnected) {
        isDisconnected = disconnected;
    }

    public LocalDate getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDate dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    // 4. toString() method (helps when printing to console)
    @Override
    public String toString() {
        return "Institution{" +
                "id=" + institutionId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", isReady=" + isReady +
                '}';
    }
}