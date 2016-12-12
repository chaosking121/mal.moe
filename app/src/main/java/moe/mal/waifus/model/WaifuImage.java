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
    @SerializedName("folder")
    @Expose
    private String folder;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("image_id")
    @Expose
    private Integer imageId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     *
     * @return
     * The downloadDate
     */
    public String getDownloadDate() {
        return downloadDate;
    }

    /**
     *
     * @param downloadDate
     * The download_date
     */
    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    /**
     *
     * @return
     * The folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     *
     * @param folder
     * The folder
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     *
     * @return
     * The hash
     */
    public String getHash() {
        return hash;
    }

    /**
     *
     * @param hash
     * The hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     *
     * @return
     * The imageId
     */
    public Integer getImageId() {
        return imageId;
    }

    /**
     *
     * @param imageId
     * The image_id
     */
    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    /**
     *
     * @return
     * The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     *
     * @param imageUrl
     * The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
