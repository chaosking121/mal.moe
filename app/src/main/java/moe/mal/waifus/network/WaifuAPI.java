package moe.mal.waifus.network;

import java.util.List;

import moe.mal.waifus.model.Token;
import moe.mal.waifus.model.User;
import moe.mal.waifus.model.Waifu;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface for Retrofit 2
 * Created by Arshad on 19/11/2016.
 */

public interface WaifuAPI {
    @GET("waifus")
    Call<List<Waifu>> getAllWaifus(@Header("Authorization") String authorization);

    @GET("waifus/lists/{listName}")
    Call<List<Waifu>> getWaifuList(@Path("listName") String listName, @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("waifus/lists/{listName}/add")
    Call<List<Waifu>> addWaifuToList(@Path("listName") String listName , @Field("waifus") String waifu, @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("waifus/lists/{listName}/remove")
    Call<List<Waifu>> removeWaifuFromList(@Path("listName") String listName, @Field("waifus") String waifu, @Header("Authorization") String authorization);

    @GET("waifus/token")
    Call<Token> getToken(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("waifus/users")
    Call<User> signUp(@Field("username") String username, @Field("password") String password);

    @GET("waifus/user/{username}")
    Call<User> getUserInfo(@Path("username") String username, @Header("Authorization") String authorization);

    @GET("waifus/login")
    Call<String> login(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("waifus/users/promote-self")
    Call<User> promoteSelf(@Field("token") String token, @Header("Authorization") String authorization);
}
