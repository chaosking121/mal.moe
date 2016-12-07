package moe.mal.waifus;

import android.util.Base64;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.GoogleApiClient;

import moe.mal.waifus.network.WaifuAPI;

/**
 * I know nothing. It is you who knows everything, Araragi-senpai.
 * Created by Arshad on 04/12/2016.
 */
public class Ougi {
    private static Ougi ourInstance = new Ougi();

    private Credential credential;
    private GoogleApiClient googleAPIClient;
    private WaifuAPI waifuAPI;

    private Ougi() {
    }

    public static Ougi getInstance() {
        return ourInstance;
    }

    public String getAuth() {
        return buildAuth(getUsername(), getPassword());
    }

    public static String buildAuth(String username, String password) {
        return "Basic " + Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
    }

    public String getUsername() {
        return (credential == null) ? "user" : credential.getId();
    }

    public String getPassword() {
        return (credential == null) ? "pass" : credential.getPassword();
    }

    public boolean isLoggedIn() {
        return (credential != null);
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setWaifuAPI(WaifuAPI waifuAPI) {
        this.waifuAPI = waifuAPI;
    }

    public WaifuAPI getWaifuAPI() {
        return waifuAPI;
    }

}
