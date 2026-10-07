package models;

public class Infrastructure {
    private int infrastructureId;
    private int institutionId;
    private int numUsers;
    private int numComputers;
    private int numLanNodes;
    private boolean hasLan;

    public Infrastructure() {
    }

    public Infrastructure(int institutionId, int numUsers, int numComputers, int numLanNodes, boolean hasLan) {
        this.institutionId = institutionId;
        this.numUsers = numUsers;
        this.numComputers = numComputers;
        this.numLanNodes = numLanNodes;
        this.hasLan = hasLan;

    }

    // Getters and Setters
    public int getInfrastructureId() { return infrastructureId; }
    public void setInfrastructureId(int infrastructureId) { this.infrastructureId = infrastructureId; }

    public int getInstitutionId() { return institutionId; }
    public void setInstitutionId(int institutionId) { this.institutionId = institutionId; }

    public int getNumUsers() { return numUsers; }
    public void setNumUsers(int numUsers) { this.numUsers = numUsers; }

    public int getNumComputers() { return numComputers; }
    public void setNumComputers(int numComputers) { this.numComputers = numComputers; }

    public int getNumLanNodes() { return numLanNodes; }
    public void setNumLanNodes(int numLanNodes) { this.numLanNodes = numLanNodes; }

    public boolean isHasLan() { return hasLan; }
    public void setHasLan(boolean hasLan) { this.hasLan = hasLan; }

        @Override
    public String toString() {
        return "Infrastructure{" +
                "institutionId=" + institutionId +
                ", users=" + numUsers +
                ", computers=" + numComputers +
                ", lanNodes=" + numLanNodes +
                ", hasLan=" + hasLan +
                '}';
    }
}

