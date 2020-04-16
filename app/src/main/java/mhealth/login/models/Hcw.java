package mhealth.login.models;

public class Hcw {
    private int id;
    private int facility_id;
    private int facility_department_id;
    private int cadre_id;
    private String facility_name;
    private String dob;
    private String id_number;

    public Hcw(int id, int facility_id, int facility_department_id, int cadre_id, String facility_name, String dob, String id_number) {
        this.id = id;
        this.facility_id = facility_id;
        this.facility_department_id = facility_department_id;
        this.cadre_id = cadre_id;
        this.facility_name = facility_name;
        this.dob = dob;
        this.id_number = id_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacility_id() {
        return facility_id;
    }

    public void setFacility_id(int facility_id) {
        this.facility_id = facility_id;
    }

    public int getFacility_department_id() {
        return facility_department_id;
    }

    public void setFacility_department_id(int facility_department_id) {
        this.facility_department_id = facility_department_id;
    }

    public int getCadre_id() {
        return cadre_id;
    }

    public void setCadre_id(int cadre_id) {
        this.cadre_id = cadre_id;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }
}
