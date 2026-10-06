package models;

public class ContactPerson {
    private int contactId;
    private int institutionId;
    private String fullName;
    private String phone;
    private String email;
    private String idNumber;

    public ContactPerson() {
    }

    public ContactPerson(int institutionId, String fullName, String phone, String email, String idNumber) {
        this.institutionId = institutionId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.idNumber = idNumber;
    }

    // Getters and Setters
    public int getContactId() { return contactId; }
    public void setContactId(int contactId) { this.contactId = contactId; }

    public int getInstitutionId() { return institutionId; }
    public void setInstitutionId(int institutionId) { this.institutionId = institutionId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
}