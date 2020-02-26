package mhealth.login.models;

public class User {
    private String access_token;
    private String token_type;
    private String expires_at;
    private String first_name;
    private String surname;
    private String gender;
    private String email;
    private String msisdn;
    private String created_at;
    private int id;
    private int role_id;
    private int profile_complete;


    public User(String access_token, String token_type, String expires_at, String first_name, String surname,
                String gender, String email, String msisdn, String created_at, int id, int role_id, int profile_complete) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_at = expires_at;
        this.first_name = first_name;
        this.surname = surname;
        this.gender = gender;
        this.email = email;
        this.msisdn = msisdn;
        this.created_at = created_at;
        this.id = id;
        this.role_id = role_id;
        this.profile_complete = profile_complete;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getProfile_complete() {
        return profile_complete;
    }

    public void setProfile_complete(int profile_complete) {
        this.profile_complete = profile_complete;
    }
}
