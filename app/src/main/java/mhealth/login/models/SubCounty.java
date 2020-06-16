package mhealth.login.models;

public class SubCounty {
    private int id;
    private String name;
    private String organisationunitid;

    public SubCounty(int id, String name, String organisationunitid) {
        this.id = id;
        this.name = name;
        this.organisationunitid = organisationunitid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisationunitid() {
        return organisationunitid;
    }

    public void setOrganisationunitid(String organisationunitid) {
        this.organisationunitid = organisationunitid;
    }
}
