package moe.mal.waifus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Arshad on 19/11/2016.
 */

public interface WaifuAPI {
    @GET("waifus")
    Call<List<Waifu>> getAllWaifus();

    @GET("waifus/lists/{listName}")
    Call<List<Waifu>> getWaifuList(@Path("listName") String listName);
}
