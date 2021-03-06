package mhealth.login.models;

public class Cadre {

    private int id;
    private String name;

    public Cadre(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object anotherObject) {
        if (!(anotherObject instanceof Cadre)) {
            return false;
        }
        Cadre c = (Cadre) anotherObject;
        return (this.id == c.id);
    }
}
