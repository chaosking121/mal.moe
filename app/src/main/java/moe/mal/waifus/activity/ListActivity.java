package moe.mal.waifus.activity;

import android.os.Bundle;
import android.widget.ListView;

import moe.mal.waifus.R;
import moe.mal.waifus.network.WaifuAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ListActivity extends SidebarActivity {

    WaifuAPI waifuAPI;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        super.setUpSidebar();

        listView = (ListView) findViewById(R.id.listView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url_base))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        waifuAPI = retrofit.create(WaifuAPI.class);

        setWaifus();
    }

    protected abstract void setWaifus();
}
