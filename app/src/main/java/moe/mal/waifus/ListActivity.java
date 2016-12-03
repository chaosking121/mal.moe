package moe.mal.waifus;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListActivity extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();
        String listName = ((String) extras.get("listName")).toLowerCase().replace(' ', '_');

        setWaifus(listName);
    }

    public void setWaifus(String listName) {
        final ListView favesListView = (ListView) findViewById(R.id.favesListView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url_base))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WaifuAPI waifuAPI = retrofit.create(WaifuAPI.class);

        Call<List<Waifu>> call = waifuAPI.getWaifuList(listName);

        call.enqueue(new Callback<List<Waifu>>() {
            @Override
            public void onResponse(Call<List<Waifu>> call, Response<List<Waifu>> response) {
                favesListView.setAdapter(
                        new ArrayAdapter<>(c, R.layout.waifu_entry, response.body()));
            }

            @Override
            public void onFailure(Call<List<Waifu>> call, Throwable t) {
                //
            }
        });

        favesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                displayWaifu(((Waifu) parent.getItemAtPosition(position)).getName());
            }
        });
    }
}
