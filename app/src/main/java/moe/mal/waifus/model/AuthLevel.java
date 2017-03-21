package moe.mal.waifus.model;

/**
 * Authorization Level Enum
 * Created by Arshad on 21/03/2017.
 */

public enum AuthLevel {
    USER(1), WEEB(2), MOD(3), ADMIN(4), ARSHAD(5);

    private int value;

    AuthLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
