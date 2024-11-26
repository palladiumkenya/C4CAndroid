package mhealth.login.models;

import com.orm.SugarRecord;

public class Profile extends SugarRecord {

    Hcw profile;

    public Profile() {
    }

    public Profile(Hcw profile) {
        this.profile = profile;
    }

    public Hcw getProfile() {
        return profile;
    }

    public void setProfile(Hcw profile) {
        this.profile = profile;
    }
}
