package moe.mal.waifus.model;

import android.util.Base64;

import java.util.List;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("auth_level")
    @Expose
    private Integer authLevel;

    @SerializedName("waifus")
    @Expose
    private List<String> waifus = null;

    private Credential credential;


    // JSON Stuff

    public String getUsername() {
        if (credential != null) {
            return credential.getId();
        } else if (username != null) {
            return username;
        } else {
            return "user";
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }

    public List<String> getWaifus() {
        return waifus;
    }

    public void setWaifus(List<String> waifus) {
        this.waifus = waifus;
    }

    @Override
    public String toString() {
        return String.format("%s - %d", username, authLevel);
    }

    // Credential Stuff

    public String getPassword() {
        return (credential == null) ? "pass" : credential.getPassword();
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public boolean isLoggedIn() {
        return (credential != null);
    }

    public String getAuth() {
        return buildAuth(getUsername(), getPassword());
    }

    public static String buildAuth(String username, String password) {
        return "Basic " + Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
    }


}