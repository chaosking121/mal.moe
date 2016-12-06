package moe.mal.waifus.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("auth_level")
    @Expose
    private Integer authLevel;
    @SerializedName("waifus")
    @Expose
    private List<String> waifus = null;

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The authLevel
     */
    public Integer getAuthLevel() {
        return authLevel;
    }

    /**
     *
     * @param authLevel
     * The auth_level
     */
    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }

    /**
     *
     * @return
     * The waifus
     */
    public List<String> getWaifus() {
        return waifus;
    }

    /**
     *
     * @param waifus
     * The waifus
     */
    public void setWaifus(List<String> waifus) {
        this.waifus = waifus;
    }

    @Override
    public String toString() {
        return String.format("%s - %d", username, authLevel);
    }

}