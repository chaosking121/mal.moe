package moe.mal.waifus.network;

import moe.mal.waifus.model.WaifuImage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface for Retrofit 2
 * Created by Arshad on 19/11/2016.
 */

public interface WaifuAPI {

    @GET("waifu/{waifu}")
    Call<WaifuImage> getImage(@Path("waifu") String waifu);
}
