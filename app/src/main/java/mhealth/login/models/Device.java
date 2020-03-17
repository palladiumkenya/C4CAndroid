package mhealth.login.models;

public class Device {
    private int id;
    private int facility_id;
    private String name;
    private int safety_designed;
    private String created_at;

    public Device(int id, int facility_id, String name, int safety_designed, String created_at) {
        this.id = id;
        this.facility_id = facility_id;
        this.name = name;
        this.safety_designed = safety_designed;
        this.created_at = created_at;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSafety_designed() {
        return safety_designed;
    }

    public void setSafety_designed(int safety_designed) {
        this.safety_designed = safety_designed;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
