package moe.mal.waifus.model;

/**
 * Class representing a single waifu image
 * Created by Arshad on 11/12/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaifuImage {

    @SerializedName("download_date")
    @Expose
    private String downloadDate;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("source_url")
    @Expose
    private String sourceUrl;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("waifu")
    @Expose
    private String waifu;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * No args constructor for use in serialization
     *
     */
    public WaifuImage() {
    }

    /**
     *
     * @param id
     * @param downloadDate
     * @param hash
     * @param sourceUrl
     * @param waifu
     * @param url
     * @param message
     */
    public WaifuImage(String downloadDate, String hash, String id, String sourceUrl, String url, String waifu, String message) {
        super();
        this.downloadDate = downloadDate;
        this.hash = hash;
        this.id = id;
        this.sourceUrl = sourceUrl;
        this.url = url;
        this.waifu = waifu;
        this.message = message;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return ((Long) Long.parseLong(hash, 16)).intValue();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WaifuImage) == false) {
            return false;
        }
        return this.id.equals(((WaifuImage) other).id);
    }

}

