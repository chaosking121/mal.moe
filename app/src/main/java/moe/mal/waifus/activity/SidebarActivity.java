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

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;

/**
 * Abstract activity to be extended by activities that need to show the sidebar
 * Created by Arshad on 04/12/2016.
 */

public abstract class SidebarActivity extends GenericActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        if (id == R.id.action_settings) {
            if (Ougi.getInstance().getUser().isLoggedIn()) {
                showScreen(ProfileActivity.class);
            } else {
                showScreen(LoginActivity.class);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        } else if ((id == R.id.search) && !(this instanceof SearchActivity)) {
            drawer.closeDrawer(GravityCompat.START);
            showSearchPrompt();
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }

        return false;
    }

    public void showSearchPrompt() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
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
}
