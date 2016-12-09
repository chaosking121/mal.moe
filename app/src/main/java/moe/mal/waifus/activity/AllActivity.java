package moe.mal.waifus.activity;

import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.List;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Waifu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for the screen that shows the list of all waifus
 * Created by Arshad on 04/12/2016.
 */

public class AllActivity extends ListActivity {

    @Override
    protected void refreshWaifus() {
        Call<List<Waifu>> call = Ougi.getInstance().getWaifuAPI().getWaifuList("all", Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                List<Waifu> waifus = response.body();
                Collections.sort(waifus);
                listView.setAdapter(
                        new ArrayAdapter<>(c, R.layout.waifu_entry, waifus));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Waifu>> call, Throwable t) {
                showToast("Failed to load waifus.");
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    protected void setWaifus() {
        refreshWaifus();
    }

    @Override
    public void handleLongPress(String waifu) {
        //Adding a waifu to the user's favourite list
        Call<List<Waifu>> call = Ougi.getInstance().getWaifuAPI().addWaifuToList(Ougi.getInstance().getUser().getUsername()
                , waifu, Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                showToast("Waifu added successfully!");
            }

            @Override
            public void onFailure(Call<List<Waifu>> call, Throwable t) {
                showToast("Failed to add that waifu to your favourites.");
            }
        });
    }
}
