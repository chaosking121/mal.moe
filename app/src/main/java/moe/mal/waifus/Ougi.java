package moe.mal.waifus;

import java.io.IOException;

import moe.mal.waifus.model.User;
import moe.mal.waifus.network.WaifuAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * I know nothing. It is you who knows everything, Araragi-senpai.
 * Created by Arshad on 04/12/2016.
 */

public class Ougi {
    private static Ougi ourInstance = new Ougi();

    private User user;
    private WaifuAPI waifuAPI;
    private String fcmToken;

    private Ougi() {
    }

    public static Ougi getInstance() {
        return ourInstance;
    }

    // User Stuff

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Other Stuff

    public void setWaifuAPI(WaifuAPI waifuAPI) {
        this.waifuAPI = waifuAPI;
    }

    public WaifuAPI getWaifuAPI() {
        return waifuAPI;
    }

    public void setFCMToken(String token) {
        this.fcmToken = token;
        updateFCMToken(token);
    }

    private void updateFCMToken(String token) {
        Call<ResponseBody> call = Ougi.getInstance().getWaifuAPI()
                .submitToken(token,
                        Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleSubmitTokenResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleSubmitTokenResponse(null);
            }
        });
    }

    private void handleSubmitTokenResponse(Response<ResponseBody> response) {
        try {
            if ((response == null) || (response.code() != 200) || response.body().string().isEmpty()) {
                //Failure
            } else {
                //Success
            }
        } catch (IOException e) {
            //Failure
        }
    }

}
