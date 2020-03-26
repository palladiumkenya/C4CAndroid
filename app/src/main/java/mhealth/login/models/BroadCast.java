package mhealth.login.models;

public class BroadCast {
    private int id;
    private int facility_d;
    private int cadre_id;
    private String created_by;
    private String approved_by;
    private String approved;
    private String message;
    private int audience;
    private String created_at;
    private String updated_at;
    private String facility;
    private String cadre;
    public boolean expanded = false;
    public boolean parent = false;


    public BroadCast(int id, int facility_d, int cadre_id, String created_by, String approved_by, String approved,
                     String message, int audience, String created_at, String updated_at, String facility, String cadre) {
        this.id = id;
        this.facility_d = facility_d;
        this.cadre_id = cadre_id;
        this.created_by = created_by;
        this.approved_by = approved_by;
        this.approved = approved;
        this.message = message;
        this.audience = audience;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.facility = facility;
        this.cadre = cadre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacility_d() {
        return facility_d;
    }

    public void setFacility_d(int facility_d) {
        this.facility_d = facility_d;
    }

    public int getCadre_id() {
        return cadre_id;
    }

    public void setCadre_id(int cadre_id) {
        this.cadre_id = cadre_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAudience() {
        return audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getCadre() {
        return cadre;
    }

    public void setCadre(String cadre) {
        this.cadre = cadre;
    }
}
