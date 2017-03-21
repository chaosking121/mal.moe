package moe.mal.waifus.model;

/**
 * Enum representing the different types of waifus
 * Created by Arshad on 21/03/2017.
 */

public enum WaifuType {
    GENERIC(1, "Generic"), CHARACTER(2, "Character"), SHIP(3, "Ship"), SERIES(4, "Series"), PERSON(5, "Person");

    private int value;
    private String repr;

    WaifuType(int value, String repr) {
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
