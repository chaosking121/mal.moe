package moe.mal.waifus;

import android.app.Activity;
import android.util.Base64;

/**
 * I know nothing. It is you who knows everything, Araragi-senpai.
 * Created by Arshad on 04/12/2016.
 */
public class Ougi {
    private static Ougi ourInstance = new Ougi();

    private String username;
    private String password;

    private Activity mainActivity;

    private Ougi() {
    }

    public static Ougi getInstance() {
        return ourInstance;
    }

    public String getAuth() {
        return "Basic " + Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void resetCredentials() {
        setCredentials("user", "pass");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return "user".equals(username);
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

}
