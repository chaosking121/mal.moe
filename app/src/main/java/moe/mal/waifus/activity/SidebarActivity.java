package moe.mal.waifus.activity;

import android.app.AlertDialog;
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

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.User;
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            if (Ougi.getInstance().getUser().isLoggedIn()) {
//                showScreen(ProfileActivity.class);
//            } else {
//                showScreen(LoginActivity.class);
//            }
//            return true;
//        }

        return super.onOptionsItemSelected(item);
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
     * Logs the user out and displays the login screen
     */
    protected void logout() {
        Auth.CredentialsApi.delete(mCredentialsApiClient,
                Ougi.getInstance().getUser().getCredential()).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(Result result) {
                        Ougi.getInstance().getUser().setCredential(null);
                        showScreen(LoginActivity.class);
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
