package moe.mal.waifus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Waifu implements Comparable<Waifu> {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is3D")
    @Expose
    private Boolean is3D;
    @SerializedName("isNSFW")
    @Expose
    private Boolean isNSFW;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     *
     */
    public Waifu() {
    }

    /**
     *
     * @param id the ID of this waifu
     * @param isNSFW whether or not this waifu is NSFW
     * @param count the number of images that this waifu has
     * @param name the name of this waifu
     * @param is3D whether or not this waifu is 3D
     */
    public Waifu(Integer count, Integer id, Boolean is3D, Boolean isNSFW, String name) {
        this.count = count;
        this.id = id;
        this.is3D = is3D;
        this.isNSFW = isNSFW;
        this.name = name;
    }

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The is3D
     */
    public Boolean getIs3D() {
        return is3D;
    }

    /**
     *
     * @param is3D
     * The is3D
     */
    public void setIs3D(Boolean is3D) {
        this.is3D = is3D;
    }

    /**
     *
     * @return
     * The isNSFW
     */
    public Boolean getIsNSFW() {
        return isNSFW;
    }

    /**
     *
     * @param isNSFW
     * The isNSFW
     */
    public void setIsNSFW(Boolean isNSFW) {
        this.isNSFW = isNSFW;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Waifu other) {
        return this.count - other.count;
    }

}