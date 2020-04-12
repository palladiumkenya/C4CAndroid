package mhealth.login.models;

public class Exposure {
    private int id;
    private String exposure_date;
    private String pep_date;
    private String exposure_location;
    private String exposure_type;
    private String device_used;
    private String result_of;
    private String device_purpose;
    private String exposure_when;
    private String exposure_description;
    private String patient_hiv_status;
    private String patient_hbv_status;
    private int previous_exposures;
    private String previous_pep_initiated;
    public boolean expanded = false;
    public boolean parent = false;

    public Exposure(int id, String exposure_date, String pep_date, String exposure_location, String exposure_type,
                    String device_used, String result_of, String device_purpose, String exposure_when, String exposure_description,
                    String patient_hiv_status, String patient_hbv_status, int previous_exposures, String previous_pep_initiated) {
        this.id = id;
        this.exposure_date = exposure_date;
        this.pep_date = pep_date;
        this.exposure_location = exposure_location;
        this.exposure_type = exposure_type;
        this.device_used = device_used;
        this.result_of = result_of;
        this.device_purpose = device_purpose;
        this.exposure_when = exposure_when;
        this.exposure_description = exposure_description;
        this.patient_hiv_status = patient_hiv_status;
        this.patient_hbv_status = patient_hbv_status;
        this.previous_exposures = previous_exposures;
        this.previous_pep_initiated = previous_pep_initiated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExposure_date() {
        return exposure_date;
    }

    public void setExposure_date(String exposure_date) {
        this.exposure_date = exposure_date;
    }

    public String getPep_date() {
        return pep_date;
    }

    public void setPep_date(String pep_date) {
        this.pep_date = pep_date;
    }

    public String getExposure_location() {
        return exposure_location;
    }

    public void setExposure_location(String exposure_location) {
        this.exposure_location = exposure_location;
    }

    public String getExposure_type() {
        return exposure_type;
    }

    public void setExposure_type(String exposure_type) {
        this.exposure_type = exposure_type;
    }

    public String getDevice_used() {
        return device_used;
    }

    public void setDevice_used(String device_used) {
        this.device_used = device_used;
    }

    public String getResult_of() {
        return result_of;
    }

    public void setResult_of(String result_of) {
        this.result_of = result_of;
    }

    public String getDevice_purpose() {
        return device_purpose;
    }

    public void setDevice_purpose(String device_purpose) {
        this.device_purpose = device_purpose;
    }

    public String getExposure_when() {
        return exposure_when;
    }

    public void setExposure_when(String exposure_when) {
        this.exposure_when = exposure_when;
    }

    public String getExposure_description() {
        return exposure_description;
    }

    public void setExposure_description(String exposure_description) {
        this.exposure_description = exposure_description;
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

    public int getPrevious_exposures() {
        return previous_exposures;
    }

    public void setPrevious_exposures(int previous_exposures) {
        this.previous_exposures = previous_exposures;
    }

    public String getPrevious_pep_initiated() {
        return previous_pep_initiated;
    }

    public void setPrevious_pep_initiated(String previous_pep_initiated) {
        this.previous_pep_initiated = previous_pep_initiated;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }
}
