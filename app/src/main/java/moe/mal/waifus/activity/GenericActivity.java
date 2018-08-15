package moe.mal.waifus.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract Activity intended to be extended by other concrete activities.
 * Created by Arshad on 03/12/2016.
 */

public abstract class GenericActivity extends AppCompatActivity {

    protected final Activity a = this;
    protected final Context c = this;

    protected ProgressDialog progress;

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
     * Switch to another activity
     * @param cls The class of the activity to be displayed
     * @param args a mapping of labels to values to be passed in as extras
     */
    protected void showScreen(Class cls, Map<String, String> args) {
        Intent in = new Intent(c, cls);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        for(String key : args.keySet()) {
            in.putExtra(key, args.get(key));
        }
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
     * @param cls The class of the activity to be displayed
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
     * This method displays a new view same as showScreen, but prevents getting back to this screen.
     * @param cls The class of the activity to be displayed
     * @param args a mapping of labels to values to be passed in as extras
     */
    protected void moveApp(Class cls, Map<String, String> args) {
        Intent in = new Intent(c, cls);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        for(String key : args.keySet()) {
            in.putExtra(key, args.get(key));
        }
        startActivity(in);
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
     * Verify that user input is of the appropriate length
     * @param text the user input to be verified
     * @return true if the text is not empty and is not too long
     */
    protected boolean verifyGenericInput(String text) {
        return !((text.isEmpty()) || (text.length() > 500));
    }

    /**
     * Verify that user input matches the given regular expression
     * @param text the user input to be verified
     * @param regex the regular expression to match against
     * @return true if the text matches the expression
     */
    protected boolean verifyInputWithRegex(String text, String regex) {
        return Pattern.compile(regex).matcher(text).matches();
    }


    /**
     * Private helper method to show a progress dialog
     * @param message the message to be displayed
     */
    protected void showProgress(String message) {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage(message);
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }
}
