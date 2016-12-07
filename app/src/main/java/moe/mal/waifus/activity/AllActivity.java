package moe.mal.waifus.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Waifu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllActivity extends ListActivity {

    @Override
    protected void setWaifus() {
        Call<List<Waifu>> call = waifuAPI.getWaifuList("all", Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                List<Waifu> waifus = response.body();
                Collections.sort(waifus);
                listView.setAdapter(
                        new ArrayAdapter<>(c, R.layout.waifu_entry, waifus));
            }

            @Override
            public void onFailure(Call<List<Waifu>> call, Throwable t) {
                showToast("Failed to load waifus.");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                displayWaifu(((Waifu) parent.getItemAtPosition(position)).getName());
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                addToList(((Waifu) parent.getItemAtPosition(position)).getName());
                return true;
            }
        });
    }

    public void addToList(String waifu) {
        Call<List<Waifu>> call = waifuAPI.addWaifuToList(Ougi.getInstance().getUser().getUsername()
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
