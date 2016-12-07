package moe.mal.waifus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Generic Activity intended to be extended by other concrete activities.
 * Created by Arshad on 03/12/2016.
 */

public abstract class GenericActivity extends AppCompatActivity {

    protected final Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showScreen(Class cls) {
        Intent in = new Intent(c, cls);
        startActivity(in);
    }

    protected void showScreen(Class cls, String arg_key, String arg_value) {
        Intent in = new Intent(c, cls);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        in.putExtra(arg_key, arg_value);
        startActivity(in);
    }

    protected void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    protected void displayWaifu(String waifu) {
        showScreen(ImageActivity.class, "waifu", waifu);
    }
}
