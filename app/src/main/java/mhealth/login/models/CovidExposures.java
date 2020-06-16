package mhealth.login.models;

public class CovidExposures {
    private int id;
    private int id_no;
    private String date_of_contact;
    private String ppe_worn;
    private String ppes;
    private String ipc_training;
    private String place_of_diagnosis;
    private String symptoms;
    private String pcr_test;
    private String management;
    private String isolation_start_date;

    public boolean expanded = false;
    public boolean parent = false;

    public CovidExposures(int id, int id_no, String date_of_contact, String ppe_worn, String ppes, String ipc_training, String place_of_diagnosis, String symptoms,String pcr_test,String management, String isolation_start_date) {
        this.id = id;
        this.id_no = id_no;
        this.date_of_contact = date_of_contact;
        this.ppe_worn = ppe_worn;
        this.ppes = ppes;
        this.ipc_training = ipc_training;
        this.place_of_diagnosis = place_of_diagnosis;
        this.symptoms = symptoms;
        this.pcr_test = pcr_test;
        this.management = management;
        this.isolation_start_date = isolation_start_date;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_no() {
        return id_no;
    }

    public void setId_no(int id_no) {
        this.id_no = id_no;
    }

    public String getDate_of_contact() {
        return date_of_contact;
    }

    public void setDate_of_contact(String date_of_contact) {
        this.date_of_contact = date_of_contact;
    }

    public String getPpe_worn() {
        return ppe_worn;
    }

    public void setPpe_worn(String ppe_worn) {
        this.ppe_worn = ppe_worn;
    }

    public String getPpes() {
        return ppes;
    }

    public void setPpes(String ppes) {
        this.ppes = ppes;
    }

    public String getIpc_training() {
        return ipc_training;
    }

    public void setIpc_training(String ipc_training) {
        this.ipc_training = ipc_training;
    }

    public String getPlace_of_diagnosis() {
        return place_of_diagnosis;
    }

    public void setPlace_of_diagnosis(String place_of_diagnosis) {
        this.place_of_diagnosis = place_of_diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPcr_test() {
        return pcr_test;
    }

    public void setPcr_test(String pcr_test) {
        this.pcr_test = pcr_test;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getIsolation_start_date() {
        return isolation_start_date;
    }

    public void setIsolation_start_date(String isolation_start_date) {
        this.isolation_start_date = isolation_start_date;
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