package mhealth.login.models;

import com.orm.SugarRecord;

public class Token extends SugarRecord {

    User token;

    public Token() {
    }

    public Token(User token) {
        this.token = token;
    }

    public User getToken() {
        return token;
    }

    public void setToken(User token) {
        this.token = token;
    }
}
