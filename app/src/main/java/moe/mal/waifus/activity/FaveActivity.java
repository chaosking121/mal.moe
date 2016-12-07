package moe.mal.waifus.activity;

import android.widget.ArrayAdapter;

import java.util.List;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.User;
import moe.mal.waifus.model.Waifu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Arshad on 04/12/2016.
 */

public class FaveActivity extends ListActivity {

    @Override
    protected void refreshWaifus() {
        Call<User> call = Ougi.getInstance().getWaifuAPI().getUserInfo(Ougi.getInstance().getUser().getUsername(),
                Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                listView.setAdapter(
                        new ArrayAdapter<>(c, R.layout.waifu_entry, response.body().getWaifus()));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showToast("Failed to load waifus.");
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    protected void setWaifus() {
        listView.setAdapter(
                new ArrayAdapter<>(c, R.layout.waifu_entry, Ougi.getInstance().getUser().getWaifus()));
    }

    @Override
    public void handleLongPress(String waifu) {
        //Removing a waifu from the user's favourite list
        Call<List<Waifu>> call = Ougi.getInstance().getWaifuAPI().removeWaifuFromList(
                Ougi.getInstance().getUser().getUsername(),
                waifu,
                Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                showToast("Waifu removed successfully.");
                refreshWaifus();
            }

            @Override
            public void onFailure(Call<List<Waifu>> call, Throwable t) {
                showToast("Failed to remove that waifu from your favourites.");
            }
        });
    }
}
