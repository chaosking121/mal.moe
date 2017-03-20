package moe.mal.waifus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Waifu implements Comparable<Waifu> {

    @SerializedName("series")
    @Expose
    private String series;
    @SerializedName("waifu_type")
    @Expose
    private Integer waifuType;
    @SerializedName("thumbnail_large")
    @Expose
    private String thumbnailLarge;
    @SerializedName("thumbnail_small")
    @Expose
    private String thumbnailSmall;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("min_auth_level")
    @Expose
    private Integer minAuthLevel;
    @SerializedName("name")
    @Expose
    private String name;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Integer getWaifuType() {
        return waifuType;
    }

    public void setWaifuType(Integer waifuType) {
        this.waifuType = waifuType;
    }

    public String getThumbnailLarge() {
        return thumbnailLarge;
    }

    public void setThumbnailLarge(String thumbnailLarge) {
        this.thumbnailLarge = thumbnailLarge;
    }

    public String getThumbnailSmall() {
        return thumbnailSmall;
    }

    public void setThumbnailSmall(String thumbnailSmall) {
        this.thumbnailSmall = thumbnailSmall;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMinAuthLevel() {
        return minAuthLevel;
    }

    public void setMinAuthLevel(Integer minAuthLevel) {
        this.minAuthLevel = minAuthLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Waifu other) {
        return this.name.compareTo(other.getName());
    }
}