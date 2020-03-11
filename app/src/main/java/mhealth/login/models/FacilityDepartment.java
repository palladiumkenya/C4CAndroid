package mhealth.login.models;

public class FacilityDepartment {
    private int id;
    private int facility_id;
    private String department_name;

    public FacilityDepartment(int id, int facility_id, String department_name) {
        this.id = id;
        this.facility_id = facility_id;
        this.department_name = department_name;
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

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}
