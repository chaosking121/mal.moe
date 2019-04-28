package moe.mal.waifus.model;

/**
 * Class representing a single waifu image
 * Created by Arshad on 11/12/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaifuImage {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("waifu")
    @Expose
    private String waifu;

    /**
     * No args constructor for use in serialization
     *
     */
    public WaifuImage() {
    }

    /**
     *
     * @param waifu
     * @param url
     */
    public WaifuImage(String url, String waifu) {
        this.url = url;
        this.waifu = waifu;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWaifu() {
        return waifu;
    }

    public void setWaifu(String waifu) {
        this.waifu = waifu;
    }

}

