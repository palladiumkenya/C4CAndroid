package mhealth.login.models;

public class Disease {
    private int id;
    private String name;
    private int doses;
    private int intervals;

    public Disease(int id, String name, int doses, int intervals) {
        this.id = id;
        this.name = name;
        this.doses = doses;
        this.intervals = intervals;
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

    public int getDoses() {
        return doses;
    }

    public void setDoses(int doses) {
        this.doses = doses;
    }

    public int getIntervals() {
        return intervals;
    }

    public void setIntervals(int intervals) {
        this.intervals = intervals;
    }
}
