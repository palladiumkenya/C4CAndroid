package mhealth.login.models;

public class Exposure {
    private int id;
    private int device_id;
    private String device_name;
    private String date;
    private String type;
    private String location;
    private String description;
    private int previous_exposures;
    private String patient_hiv_status;
    private String patient_hbv_status;
    private int pep_initiated;
    private String device_purpose;
    public boolean expanded = false;
    public boolean parent = false;

    public Exposure(int id, int device_id, String device_name, String date, String type, String location, String description,
                    int previous_exposures, String patient_hiv_status, String patient_hbv_status, int pep_initiated, String device_purpose) {
        this.id = id;
        this.device_id = device_id;
        this.device_name = device_name;
        this.date = date;
        this.type = type;
        this.location = location;
        this.description = description;
        this.previous_exposures = previous_exposures;
        this.patient_hiv_status = patient_hiv_status;
        this.patient_hbv_status = patient_hbv_status;
        this.pep_initiated = pep_initiated;
        this.device_purpose = device_purpose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrevious_exposures() {
        return previous_exposures;
    }

    public void setPrevious_exposures(int previous_exposures) {
        this.previous_exposures = previous_exposures;
    }

    public String getPatient_hiv_status() {
        return patient_hiv_status;
    }

    public void setPatient_hiv_status(String patient_hiv_status) {
        this.patient_hiv_status = patient_hiv_status;
    }

    public String getPatient_hbv_status() {
        return patient_hbv_status;
    }

    public void setPatient_hbv_status(String patient_hbv_status) {
        this.patient_hbv_status = patient_hbv_status;
    }

    public int getPep_initiated() {
        return pep_initiated;
    }

    public void setPep_initiated(int pep_initiated) {
        this.pep_initiated = pep_initiated;
    }

    public String getDevice_purpose() {
        return device_purpose;
    }

    public void setDevice_purpose(String device_purpose) {
        this.device_purpose = device_purpose;
    }
}
