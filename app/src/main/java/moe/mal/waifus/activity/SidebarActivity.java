package moe.mal.waifus.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;

import java.io.IOException;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Abstract activity to be extended by activities that need to show the sidebar
 * Created by Arshad on 04/12/2016.
 */

public abstract class SidebarActivity extends AuthActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Method to be called by subclasses to setup the sidebar for their activity
     */
    protected void setUpSidebar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            showScreen(AboutActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleScrapeResponse(Response<ResponseBody> response) {

        if ((response == null) || (response.code() != 200)) {
            showToast("Scraping failed.");
        } else {
            try {
                showToast(response.body().string().isEmpty() ? "Added to scraping queue." : response.body().string());
            } catch (IOException e) {
                showToast("Scraping failed.");
            }
        }
    }

    private void attemptToScrape(String waifu, String url) {

        Call<ResponseBody> call = Ougi.getInstance().getWaifuAPI()
                .scrape(waifu, url,
                        Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleScrapeResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleScrapeResponse(null);
            }
        });
    }

    protected void showScrapePrompt() {
        showScrapePrompt(null);
    }

    /**
     * Displays a prompt asking the user to specify a waifu to view
     */
    protected void showScrapePrompt(String waifu) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_scrape, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        final EditText waifuField = (EditText) mView.findViewById(R.id.waifuField);
        final EditText urlField = (EditText) mView.findViewById(R.id.urlField);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

            if (item.getText() != null) {
                urlField.setText(item.getText());
            }
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if ((!verifyPromptText(waifuField.getText().toString()))
                        || !verifyPromptText(urlField.getText().toString())) {
                            showToast("Invalid input.");
                            return;
                        }
                        attemptToScrape(waifuField.getText().toString(),urlField.getText().toString());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        if (waifu != null) {
            waifuField.setText(waifu);
            waifuField.setFocusable(false);
            urlField.requestFocus();
        }

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
    /**
     * Displays a prompt asking the user to specify a waifu to view
     */
    protected void showSearchPrompt() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_search, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if (!verifyPromptText(userInputDialogEditText.getText().toString())) {
                            showToast("Invalid input.");
                            return;
                        }
                        displayWaifu(userInputDialogEditText.getText().toString());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    /**
     * Displays a prompt asking the user to enter a promotion code
     */
    protected void showPromotionPrompt() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_promotion, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if (!verifyPromptText(userInputDialogEditText.getText().toString())) {
                            showToast("Invalid input.");
                            return;
                        }
                        tryToPromoteSelf(userInputDialogEditText.getText().toString());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    /**
     * Makes the call to the promote-self endpoint
     * @param token the promotion code provided by the user
     */
    private void tryToPromoteSelf(String token) {
        Call<User> call = Ougi.getInstance().getWaifuAPI()
                .promoteSelf(token,
                        Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                handlePromoteResponse(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                handlePromoteResponse(null);
            }
        });
    }

    /**
     * Handles the response generated by the promotion API call
     * @param response the response generated
     */
    private void handlePromoteResponse(Response<User> response) {
        if ((response == null) || (response.code() != 200)) {
            showToast("Unable to promote you.");
            return;
        }

        Credential buffer = Ougi.getInstance().getUser().getCredential();

        User user = response.body();
        user.setCredential(buffer);
        Ougi.getInstance().setUser(user);

        showToast(String.format("Promoted successfully to level %d.", user.getAuthLevel()));
    }

    private boolean verifyPromptText(String text) {
        return !((text.isEmpty()) || (text.length() > 300));
    }

    /**
     * Logs the user out and displays the login screen
     */
    protected void logout() {
        Auth.CredentialsApi.delete(mCredentialsApiClient,
                Ougi.getInstance().getUser().getCredential()).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(Result result) {
                        Ougi.getInstance().getUser().setCredential(null);
                        moveApp(LoginActivity.class);
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if ((id == R.id.sad) && !(this instanceof SadActivity)) {
            showScreen(SadActivity.class);
            drawer.closeDrawer(GravityCompat.START);
        } else if ((id == R.id.faves) && !(this instanceof FaveActivity)) {
            showScreen(FaveActivity.class);
            drawer.closeDrawer(GravityCompat.START);
        } else if ((id == R.id.all) && !(this instanceof AllActivity)) {
            showScreen(AllActivity.class);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.search) {
            drawer.closeDrawer(GravityCompat.START);
            showSearchPrompt();
        } else if (id == R.id.scrape) {
            drawer.closeDrawer(GravityCompat.START);
            showScrapePrompt();
        } else if (id == R.id.promote) {
            drawer.closeDrawer(GravityCompat.START);
            showPromotionPrompt();
        } else if (id == R.id.logout) {
            drawer.closeDrawer(GravityCompat.START);
            logout();
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }

        return false;
    }
}
