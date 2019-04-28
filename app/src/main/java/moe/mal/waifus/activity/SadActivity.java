package moe.mal.waifus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.network.WaifuAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sad);

        Ougi.getInstance().setWaifuAPI(new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url_base))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WaifuAPI.class));
    }

    /**
     * Handles when the lily button is pressed
     * @param v the view
     */
    public void lilySelected(View v) {
        displayWaifu("lily");
    }

    /**
     * Handles when the sad button is pressed
     * @param v the view
     */
    public void sadSelected(View v) {
        displayWaifu("sad");
    }

    /**
     * Wrapper method to display a particular waifu
     * @param waifu the waifu to be displayed
     */
    private void displayWaifu(String waifu) {
        Intent in = new Intent(this, ImageActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        in.putExtra("waifu", waifu);
        startActivity(in);
    }
}
