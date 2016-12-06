package moe.mal.waifus.activity;

import android.view.View;
import android.widget.AdapterView;
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
    protected void setWaifus() {
        Call<User> call = waifuAPI.getUserInfo(Ougi.getInstance().getUsername(), Ougi.getInstance().getAuth());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                listView.setAdapter(
                        new ArrayAdapter<>(c, R.layout.waifu_entry, response.body().getWaifus()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showToast("Failed to load waifus.");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                displayWaifu(((String) parent.getItemAtPosition(position)));
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeFromList(((String) parent.getItemAtPosition(position)));
                return true;
            }
        });
    }

    public void removeFromList(String waifu) {
        Call<List<Waifu>> call = waifuAPI.removeWaifuFromList(Ougi.getInstance().getUsername(), waifu, Ougi.getInstance().getAuth());

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                showToast("Waifu removed successfully.");
                setWaifus();
            }

            @Override
            public void onFailure(Call<List<Waifu>> call, Throwable t) {
                showToast("Failed to remove that waifu from your favourites.");
            }
        });
    }
}
