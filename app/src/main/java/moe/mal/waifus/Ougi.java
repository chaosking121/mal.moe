package moe.mal.waifus;

import moe.mal.waifus.network.WaifuAPI;

/**
 * I know nothing. It is you who knows everything, Araragi-senpai.
 * Created by Arshad on 04/12/2016.
 */

public class Ougi {
    private static Ougi ourInstance = new Ougi();

    private WaifuAPI waifuAPI;

    private Ougi() {
    }

    public static Ougi getInstance() {
        return ourInstance;
    }

    // Other Stuff

    public void setWaifuAPI(WaifuAPI waifuAPI) {
        this.waifuAPI = waifuAPI;
    }

    public WaifuAPI getWaifuAPI() {
        return waifuAPI;
    }

}
