package moe.mal.waifus.activity;

import android.graphics.drawable.Drawable;

import java.util.Collections;
import java.util.List;

import butterknife.BindDrawable;
import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Waifu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for the screen that shows the list of a user's favourite waifus
 * Created by Arshad on 04/12/2016.
 */

public class FaveActivity extends ListActivity {

    @BindDrawable(R.drawable.ic_close_black_24dp) Drawable entryIcon;

    @Override
    protected void loadWaifus() {
        swipeContainer.setRefreshing(true);
        List<Waifu> existingWaifus = Ougi.getInstance().getFaveWaifusList();
        if (existingWaifus != null) {
            waifus.addAll(existingWaifus);
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        } else {
            refreshWaifus();
        }
    }

    @Override
    protected void refreshWaifus() {
        Call<List<Waifu>> call = Ougi.getInstance().getWaifuAPI().getWaifuList(Ougi.getInstance().getUser().getUsername(),
                Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                List<Waifu> newWaifus = response.body();
                Collections.sort(newWaifus);
                Ougi.getInstance().setFaveWaifusList(newWaifus);
                waifus.clear();
                waifus.addAll(newWaifus);
                adapter.notifyDataSetChanged();
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
    public void handleListAction(String waifu) {
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

    @Override
    public boolean isAll() {
        return false;
    }

    @Override
    public Drawable getListActionIcon() {
        return entryIcon;
    }

    @Override
    public String getListActionTitle() {
        return "Remove From Faves";
    }
}
