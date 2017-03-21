package moe.mal.waifus.model;

/**
 * Created by Arshad on 21/03/2017.
 */

public enum WaifuAuthLevel {
    SFW(1, "SFW"), POSSIBLY_NSFW(2, "Possibly NSFW"), DEFINITELY_NSFW(5, "Definitely NSFW"), THREE_D(6, "3D");

    private int value;
    private String repr;

    WaifuAuthLevel(int value, String repr) {
        this.value = value;
        this.repr = repr;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return repr;
    }
}
