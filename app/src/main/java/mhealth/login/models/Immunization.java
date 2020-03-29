package mhealth.login.models;

import java.util.List;

public class Immunization {
    private int disease_id;
    private String disease;
    private List<String> immunizations;
    public boolean expanded = false;
    public boolean parent = false;

    public Immunization(int disease_id, String disease, List<String> immunizations) {
        this.disease_id = disease_id;
        this.disease = disease;
        this.immunizations = immunizations;
    }

    public int getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(int disease_id) {
        this.disease_id = disease_id;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public List<String> getImmunizations() {
        return immunizations;
    }

    public void setImmunizations(List<String> immunizations) {
        this.immunizations = immunizations;
    }
}
