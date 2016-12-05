package moe.mal.waifus.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Waifu;
import moe.mal.waifus.network.WaifuAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListActivity extends SidebarActivity {

    WaifuAPI waifuAPI;
    String listName;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        super.setUpSidebar();

        Bundle extras = getIntent().getExtras();

        listName = ((String) extras.get("listName"));
        listName = (listName == null) ? "all" : listName.toLowerCase().replace(' ', '_');

        if (listName.equals("faves")) {
            listName = Ougi.getInstance().getUsername();
        }

        listView = (ListView) findViewById(R.id.listView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url_base))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        waifuAPI = retrofit.create(WaifuAPI.class);

        setWaifus(listName);
    }

    public void setWaifus(String listName) {
        Call<List<Waifu>> call = waifuAPI.getWaifuList(listName, Ougi.getInstance().getAuth());

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                listView.setAdapter(
                        new ArrayAdapter<>(c, R.layout.waifu_entry, response.body()));
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

        if (listName.equals("all")) {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    addToList(((Waifu) parent.getItemAtPosition(position)).getName());
                    return true;
                }
            });
        } else {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    removeFromList(((Waifu) parent.getItemAtPosition(position)).getName());
                    return true;
                }
            });
        }
    }

    public void addToList(String waifu) {
        Call<List<Waifu>> call = waifuAPI.addWaifuToList(Ougi.getInstance().getUsername()
                , waifu, Ougi.getInstance().getAuth());

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

    public void removeFromList(String waifu) {
        Call<List<Waifu>> call = waifuAPI.removeWaifuFromList(listName, waifu, Ougi.getInstance().getAuth());

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                showToast("Waifu removed successfully.");
                setWaifus(listName);
            }

            @Override
            public void onFailure(Call<List<Waifu>> call, Throwable t) {
                showToast("Failed to remove that waifu from your favourites.");
            }
        });
    }
}
