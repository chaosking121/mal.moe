package moe.mal.waifus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Abstract Activity intended to be extended by other concrete activities.
 * Created by Arshad on 03/12/2016.
 */

public abstract class GenericActivity extends AppCompatActivity {

    protected final Activity a = this;
    protected final Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    /**
     * Switch to another activity
     * @param cls The class of the activity to be displayed
     */
    protected void showScreen(Class cls) {
        Intent in = new Intent(c, cls);
        startActivity(in);
    }

    /**
     * Switch to another activity
     * @param cls The class of the activity to be displayed
     * @param arg_key the key of an argument to be passed along to the new activity
     * @param arg_value the value of an argument to be passed along to the new activity
     */
    protected void showScreen(Class cls, String arg_key, String arg_value) {
        Intent in = new Intent(c, cls);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        in.putExtra(arg_key, arg_value);
        startActivity(in);
    }

    /**
     * This method displays a new view same as showScreen, but prevents getting back to this screen.
     * @param cls
     */
    protected void moveApp(Class cls) {
        Intent in = new Intent(c, cls);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        this.finish();
    }

    /**
     * This method displays a new view same as showScreen, but prevents getting back to this screen.
     * @param cls
     * @param arg_key the key of an argument to be passed along to the new activity
     * @param arg_value the value of an argument to be passed along to the new activity
     */
    protected void moveApp(Class cls, String arg_key, String arg_value) {
        Intent in = new Intent(c, cls);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtra(arg_key, arg_value);
        startActivity(in);
        this.finish();
    }

    /**
     * Helper method to show a short toast
     * @param message the message to be shown in the toast
     */
    protected void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /**
     * Wrapper method to display a particular waifu
     * @param waifu the waifu to be displayed
     */
    protected void displayWaifu(String waifu) {
        showScreen(ImageActivity.class, "waifu", waifu);
    }

    /**
     * Verify that user input is of the appropriate legnth
     * @param text the user input to be verified
     * @return true if the text is not empty and is not too long
     */
    protected boolean verifyGenericInput(String text) {
        return !((text.isEmpty()) || (text.length() > 500));
    }

}
